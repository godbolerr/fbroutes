package com.work.fb;

import javax.inject.Inject;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.work.fb.domain.FbPost;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(FbRoutesApplication.class)
public class PostToFb {


	@Inject
	private ApplicationContext appContext;
	
	@Test
	public void postUrl() {
		
		TokenProcessor processor = appContext.getBean(TokenProcessor.class);
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer "+ processor.getJwtToken("test"));
		
		long timeStamp = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			FbPost post = new FbPost();
			String label =  timeStamp + " " + i;
			post.setUrl("http://www.google.com/?code=" + label);
			post.setCaption(label);
			post.setDescription(label);
			HttpEntity<FbPost> entity = new HttpEntity<FbPost>(post,headers);
			ResponseEntity<FbPost> tokenResult = restTemplate.exchange("http://35.164.126.228:9999/api/fbposts",HttpMethod.POST, entity, FbPost.class);
			System.out.println("#### " + tokenResult.getBody());

			
		}
//		post.setUrl("http://www.google.com/?code=" + time);
//		HttpEntity<FbPost> entity = new HttpEntity<FbPost>(post,headers);
//		ResponseEntity<FbPost> tokenResult = restTemplate.exchange("http://35.164.126.228:9999/api/fbposts",HttpMethod.POST, entity, FbPost.class);
//		System.out.println("#### " + tokenResult.getBody());
		
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DefaultCamelContext camelContext = appContext.getBean(DefaultCamelContext.class);
		
		ProducerTemplate pTemplate = camelContext.createProducerTemplate();
		
		//pTemplate.sendBody("direct:postToFb", "Test");
		
		

		
		
		
		
		
		
		
		
	}

}
