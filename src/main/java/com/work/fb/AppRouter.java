package com.work.fb;

import java.net.URL;

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

import facebook4j.PrivacyParameter;

@Component
public class AppRouter extends FatJarRouter {
	
	@Inject
	private ApplicationContext appContext;
	
	FacebookConfiguration configuration = null;
	
	@Override
	public void configure() throws Exception {
		
//        configuration.setOAuthAppId("1633262880316027");
//        configuration.setOAuthAppSecret("76c7dc48241579ba5168b11db5327481");
//        configuration.setOAuthAccessToken("EAAXNcaXOWnsBAI1syZAM8KHVt7qevWUe3jA25y4qdylLG64SuBxX6bZAZC0LRRq4IRy9QJBvG70ZCHaFvbef0W8gqfdcmeSvPTYWQ2NGjP57eJmyOokOdvg23DKrbKir05wUQWeIQ0zpTC2F2joYjgPaZBZBs3FwFXrXTPsWyOTp3oobl5IKJb87o3VwJucDkZD");
//	      configuration.setHttpProxyHost("proxy.cognizant.com");
//	      configuration.setHttpProxyPort(6050);
//	      configuration.setHttpProxyUser("115750");
//	      configuration.setHttpProxyPassword("Spiti098");
//	      configuration.setPrettyDebugEnabled(true);
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
          .description("Send Ofs Message")
          .consumes("application/json")
          .produces("application/json")
          .post().type(FbPost.class)
          .route()
          .routeId("Rest")
          .log("Sending message ${body}")
          .to("direct:feed");
		  
		  
          from("direct:feed").bean(RefreshFbToken.class).bean(PostFbUpdate.class,"prepare")              

          .to("facebook://postFeed?inBody=postUpdate")     
          .process(new Processor() {
              public void process(Exchange exchange) throws Exception {
               
            	  System.out.println(exchange.getIn().getHeaders());
                  
                  
              }
          })
          .bean(PostFbUpdate.class,"update");
		  
		
	}

}
