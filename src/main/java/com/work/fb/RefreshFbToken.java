package com.work.fb;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.component.facebook.config.FacebookConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;

@Component
public class RefreshFbToken {

	@Inject
	private ApplicationContext appContext;
	
	@Inject
	private Environment env;
	

	public void refreshToken(Exchange exchange) {

		FacebookConfiguration configuration = appContext.getBean(FacebookConfiguration.class);

		ConfigurationBuilder confb = new ConfigurationBuilder();
		
		String proxyEnabled = env.getProperty("fb.proxyEnabled");

		if (proxyEnabled != null && proxyEnabled.equalsIgnoreCase("true")) {


			confb.setHttpProxyHost(env.getProperty("fb.proxyHost"));
			confb.setHttpProxyPort(env.getProperty("fb.proxyPort", Integer.class));
			confb.setHttpProxyUser(env.getProperty("fb.proxyUser"));
			confb.setHttpProxyPassword(env.getProperty("fb.proxyPassword"));

		}
		
		FacebookFactory ff = new FacebookFactory(confb.build());
		Facebook facebook = ff.getInstance();

		facebook.setOAuthAppId(configuration.getOAuthAppId(), configuration.getOAuthAppSecret());
		String shortLivedToken = configuration.getOAuthAccessToken();
		AccessToken extendedToken;
		try {
			extendedToken = facebook.extendTokenExpiration(shortLivedToken);
			configuration.setOAuthAccessToken(extendedToken.getToken());
			System.out.println("Token extended : " + extendedToken.getToken());			
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}

}
