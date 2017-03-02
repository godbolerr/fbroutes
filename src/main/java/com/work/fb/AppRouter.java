package com.work.fb;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.facebook.FacebookComponent;
import org.apache.camel.component.facebook.config.FacebookConfiguration;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.work.fb.domain.FbPost;

@Component
public class AppRouter extends FatJarRouter {
	
	@Inject
	private ApplicationContext appContext;
	
	@Inject
	private Environment env;

	
	FacebookConfiguration configuration = null;
	
	@Value("${fb.fbPostHostIp}")
	String fbPostHostIp ;
	
	@Value("${fb.fbPostHostPort}")
	String fbPostHostPort ;	
	
	
	@Override
	public void configure() throws Exception {
		configuration = appContext.getBean(FacebookConfiguration.class);
		
		
		
		
		
		// Set the token from the rest url
		
		TokenProcessor processor = appContext.getBean(TokenProcessor.class);
		configuration.setOAuthAccessToken(processor.getCurrentFbToken().getuToken());
        
		System.out.println("OAUTH TOKEN IN THE CONFIGURATION : " + configuration.getOAuthAccessToken());
		
        FacebookComponent fbc = getContext().getComponent("facebook", FacebookComponent.class);
        fbc.setConfiguration(configuration);        
		
		// configure to use jetty on localhost with the given port and enable auto binding mode
        restConfiguration()
                .component("jetty")
                .host("0.0.0.0").port(9090)
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("json.in.disableFeatures", "FAIL_ON_UNKNOWN_PROPERTIES, ADJUST_DATES_TO_CONTEXT_TIME_ZONE")
                .dataFormatProperty("json.in.enableFeatures", "FAIL_ON_NUMBERS_FOR_ENUMS, USE_BIG_DECIMAL_FOR_FLOATS")
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "API")
                .apiProperty("api.version", "1.0.0")
                .apiProperty("cors", "true");
		
		  rest("/updateUrl")
          .description("Send Message")
          .consumes("application/json")
          .produces("application/json")
          .post().type(FbPost.class)
          .route()
          .routeId("Rest")
          .log("Sending message ${body}")
          .to("direct:feed");

			 onException(HttpOperationFailedException.class)
			  .process(new Processor() { 
                  public void process(Exchange exchange) throws Exception { 
                	  Exception c = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, 
                			  Exception.class); 
                      System.out.println("INBODY ----------------"+exchange.getIn().getBody(String.class)); 
                      System.out.println("INHEAD ----------------"+exchange.getIn().getHeaders()); 
                      System.out.println("OUTBODY ---------------"+exchange.getOut().getBody(String.class)); 
                      System.out.println("OUTHEAD ---------------"+exchange.getOut().getHeaders()); 
                      System.out.println("EXC -------------------"+c.getMessage() + ":" + c.getCause()); 
                      System.out.println("FAIL ------------------"+exchange.isFailed()); 
                  } 
              })  
	           .log("HTTP exception handled")
	           .bean(PostFbUpdate.class,"handleNotFound")
	           .handled(true)
	           //.continued(true)
	           .setBody(constant("{\"status\":\"404\"}"));
		  
          from("direct:feed").bean(TokenProcessor.class).bean(PostFbUpdate.class,"prepare")              

          .to("facebook://postFeed?inBody=postUpdate")     
          .process(new Processor() {
              public void process(Exchange exchange) throws Exception {
               
            	  System.out.println(exchange.getIn().getHeaders());
                  
                  
              }
          })
          .bean(PostFbUpdate.class,"update");

          
       	  rest("/getJwtToken")
          .get()
          .route()
          .routeId("getJwtToken").bean(TokenProcessor.class,"getJwtToken");
        
		  
    	  rest("/getPost")
          .get()
          .route()
          .routeId("getPost")
          .to("direct:getPost");
	
          
		 from("direct:getPost")
		 .bean(TokenProcessor.class,"getJwtAccessToken")
		 .to("http4://"+ fbPostHostIp +  ":" + fbPostHostPort + "/api/fbposts/getNext?bridgeEndpoint=true")
		 .bean(PostFbUpdate.class,"parseJsonPost")
		 .bean(TokenProcessor.class,"refreshToken")
		 .bean(PostFbUpdate.class,"prepare")
		 .to("facebook://postFeed?inBody=postUpdate")
		 .recipientList(simple("http4://" + fbPostHostIp + ":" + fbPostHostPort + "/api/fbposts/updateObjectId/${header.POST_ID}/${body}?bridgeEndpoint=true"))
		 .bean(PostFbUpdate.class,"parseJsonPost")
		 .log("Received ${body}");
		  		 
		 //from("quartz2://myGroup/myTimerName?cron=0+0/5+12-18+?+*+MON-FRI&fireNow=true").to("direct:getPost");
		 
		 
		 from("quartz2://myGroup/myTimerName?cron=0+0+*/3+*+*+?")
		  //from("direct:postToFb")
		 .bean(TokenProcessor.class,"getJwtAccessToken")
		  .process(new Processor() { 
              public void process(Exchange exchange) throws Exception { 
            	  exchange.getIn().setHeader("CamelHttpMethod","GET");
              } 
          })  

		 .to("http4://"+ fbPostHostIp +  ":" + fbPostHostPort + "/api/fbposts/getNext")
		 .bean(PostFbUpdate.class,"parseJsonPost")
		 .bean(TokenProcessor.class,"refreshToken")
		 .bean(PostFbUpdate.class,"prepare")
		 .to("facebook://postFeed?inBody=postUpdate")
		 .recipientList(simple("http4://" + fbPostHostIp + ":" + fbPostHostPort + "/api/fbposts/updateObjectId/${header.POST_ID}/${body}/"))
		 .bean(PostFbUpdate.class,"parseJsonPost")
		 .log("Received ${body}");
	}

}
