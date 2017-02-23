package com.work.fb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.work.fb.domain.FbPost;

import facebook4j.PrivacyParameter;

@Component
public class PostFbUpdate {

	public void prepare(Exchange exchange) {

		FbPost post = (FbPost) exchange.getIn().getBody();

		String desc = post.getDescription();

		if (desc == null) {
			desc = "";
		}

		facebook4j.PostUpdate pu = new facebook4j.PostUpdate(desc);

		PrivacyParameter pp = new PrivacyParameter();

		if (post.getPrivacy() == null) {
			pp.setValue("SELF");
		} else {
			pp.setValue(post.getPrivacy());
		}

		pu.setPrivacy(pp);
		try {
			if (post.getUrl() != null) {
				pu.setLink(new URL(post.getUrl()));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		exchange.getIn().setHeader("POST_ID", post.getId());
		exchange.getIn().setBody(pu);
	}

	public void update(Exchange exchange) {

		String objectId = (String) exchange.getIn().getBody();

		System.out.println("Setting object id " + objectId + " for url " + exchange.getIn().getHeader("POST_ID"));

	}

	public void parseJsonPost(Exchange exchange) {

		String jsonString = exchange.getIn().getBody(String.class);

		ObjectMapper mapper = new ObjectMapper();

		try {
			FbPost post = mapper.readValue(jsonString, FbPost.class);
			exchange.getIn().setBody(post);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
