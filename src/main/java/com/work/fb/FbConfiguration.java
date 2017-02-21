package com.work.fb;

import javax.inject.Inject;

import org.apache.camel.component.facebook.config.FacebookConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class FbConfiguration {

	//TODO
	//Can we store accessToken with timestamp so that it can be saved for some immediate requests without renewing it ?
	
	@Inject
	private Environment env;

	@Bean
	public FacebookConfiguration fbconf() {

		FacebookConfiguration conf = new FacebookConfiguration();
		conf.setOAuthAppId(env.getProperty("fb.appId"));
		conf.setOAuthAppSecret(env.getProperty("fb.appSecret"));
		conf.setOAuthAccessToken(env.getProperty("fb.userAccessToken"));
		String proxyEnabled = env.getProperty("fb.proxyEnabled");

		if (proxyEnabled != null && proxyEnabled.equalsIgnoreCase("true")) {

			conf.setHttpProxyHost(env.getProperty("fb.proxyHost"));
			conf.setHttpProxyPort(env.getProperty("fb.proxyPort", Integer.class));
			conf.setHttpProxyUser(env.getProperty("fb.proxyUser"));
			conf.setHttpProxyPassword(env.getProperty("fb.proxyPassword"));

		}

		return conf;
	}

}
