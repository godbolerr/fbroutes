package com.work.fb;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import com.work.fb.domain.FbPost;

import facebook4j.PrivacyParameter;

@Component
public class PostFbUpdate {

	public void prepare(Exchange exchange) {

		FbPost post = (FbPost) exchange.getIn().getBody();

		facebook4j.PostUpdate pu = new facebook4j.PostUpdate(post.getDescription());
		PrivacyParameter pp = new PrivacyParameter();
		pp.setValue(post.getPrivacy());
		pu.setPrivacy(pp);
		try {
			pu.setLink(new URL(post.getUrl()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		exchange.getIn().setHeader("POST_URL", post.getUrl());
		exchange.getIn().setBody(pu);
	}

	public void update(Exchange exchange) {

		String objectId = (String) exchange.getIn().getBody();

		System.out.println("Setting object id " + objectId + " for url " + exchange.getIn().getHeader("POST_URL"));

	}

}
