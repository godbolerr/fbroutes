package com.work.fb;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.facebook.FacebookComponent;
import org.apache.camel.component.facebook.config.FacebookConfiguration;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.work.fb.domain.FbPost;

@Component
public class AppRouter extends FatJarRouter {
	
	@Inject
	private ApplicationContext appContext;
	
	FacebookConfiguration configuration = null;
	
	@Override
	public void configure() throws Exception {
		configuration = appContext.getBean(FacebookConfiguration.class);
        
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
	
          
		 from("direct:getPost").bean(TokenProcessor.class,"getJwtAccessToken").to("http4:localhost:9999/api/fbposts/getNext?bridgeEndpoint=true").bean(PostFbUpdate.class,"parseJsonPost")
		 .bean(PostFbUpdate.class,"prepare")
		 .to("facebook://postFeed?inBody=postUpdate")
		 .recipientList(simple("http4:localhost:9999/api/fbposts/updateObjectId/${header.POST_ID}/${body}?bridgeEndpoint=true"))
		 .bean(PostFbUpdate.class,"parseJsonPost")
		 .log("Received ${body}");
		  
		 
		 from("quartz2://myGroup/myTimerName?cron=0+10+13+?+*+MON-FRI").to("direct:getPost");
	}

}
