package com.work.fb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.component.facebook.config.FacebookConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.conf.ConfigurationBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class RefreshFbToken {

	@Inject
	private ApplicationContext appContext;

	@Inject
	private Environment env;

	private static final String AUTHORITIES_KEY = "auth";

	private long tokenValidityInMilliseconds = 1000 * 200;
	
	private String secretKey = "jwtToken";
	
	private final String AUTHORIZATION_HEADER = "Authorization";

	public void getJwtAccessToken(Exchange exchange) {

		String token = "";

		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));

		// Create new auth token
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("test", "test",
				grantedAuths);

		String authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInMilliseconds);

		token = Jwts.builder().setSubject(auth.getName()).claim(AUTHORITIES_KEY, authorities)
				.signWith(SignatureAlgorithm.HS512, secretKey).setExpiration(validity).compact();

		exchange.getIn().setHeader(AUTHORIZATION_HEADER, "Bearer " + token);
		
		System.out.println("Sending with token " + exchange.getIn().getHeader(AUTHORIZATION_HEADER));
	}

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
