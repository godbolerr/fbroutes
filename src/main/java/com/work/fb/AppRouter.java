package com.work.fb;

import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.stereotype.Component;

@Component
public class AppRouter extends FatJarRouter {
	
	@Override
	public void configure() throws Exception {
		
		
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
          .post()
          .route()
          .routeId("Rest").convertBodyTo(String.class)
          .log("Sending message ${body}")
          .to("direct:startOfs");
		  
		  
		  from("direct:startOfs").log("Received ${body}");
		
	}

}
