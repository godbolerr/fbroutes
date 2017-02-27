package com.work.fb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.camel.Exchange;
import org.apache.camel.component.facebook.config.FacebookConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.work.fb.domain.FbUToken;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProcessor {

	@Inject
	private ApplicationContext appContext;
	
	@Value("${fb.userTokenUri}")
	String userTokenUri ;	

	@Inject
	private Environment env;

	private static final String AUTHORITIES_KEY = "auth";

	private long tokenValidityInMilliseconds = 1000 * 86400;

	private String secretKey = "jwtToken";

	private final String AUTHORIZATION_HEADER = "Authorization";

	public void getJwtAccessToken(Exchange exchange) {
		exchange.getIn().setHeader(AUTHORIZATION_HEADER, "Bearer " + getJwtToken("test"));
	}

	public FbUToken getCurrentFbToken(){
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer "+getJwtToken("test"));
		HttpEntity<FbUToken> entity = new HttpEntity<FbUToken>(new FbUToken(),headers);
		//.getForObject(userTokenUri + "/1", FbUToken.class);
		ResponseEntity<FbUToken> tokenResult = restTemplate.exchange(userTokenUri + "/1",HttpMethod.GET, entity, FbUToken.class);
		
		return tokenResult.getBody();
	
	}
	public void refreshToken(Exchange exchange) {

		FacebookConfiguration configuration = appContext.getBean(FacebookConfiguration.class);
		



		String shortLivedToken = "";
		
		// Fetch token from the Rest server.
		// if it is -1 start with the one supplied in the configuration otherwise use that.
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer "+getJwtToken("test"));
		HttpEntity<FbUToken> entity = new HttpEntity<FbUToken>(new FbUToken(),headers);
		//.getForObject(userTokenUri + "/1", FbUToken.class);
		ResponseEntity<FbUToken> tokenResult = restTemplate.exchange(userTokenUri + "/1",HttpMethod.GET, entity, FbUToken.class);
		
		FbUToken result = tokenResult.getBody();
		
		if ( result != null && result.getuToken() != null && result.getuToken().length() < 2 ){
			shortLivedToken = configuration.getOAuthAccessToken();
			System.out.println("Using local token " + shortLivedToken);

		} else {
			shortLivedToken = result.getuToken();
			configuration.setOAuthAccessToken(shortLivedToken);
			System.out.println("Received user Access Token  from the server " + shortLivedToken);
		}
		
		AccessToken extendedToken = null;
		try {
			
			ConfigurationBuilder confb = new ConfigurationBuilder();
			confb.setOAuthAccessToken(shortLivedToken);
			FacebookFactory ff = new FacebookFactory(confb.build());
			Facebook facebook = ff.getInstance();
			facebook.setOAuthAppId(configuration.getOAuthAppId(), configuration.getOAuthAppSecret());
			extendedToken = facebook.extendTokenExpiration(shortLivedToken);
			System.out.println("### Current FB User Token" + shortLivedToken);
			configuration.setOAuthAccessToken(extendedToken.getToken());
			System.out.println("### Updated FB User Token" + result.getuToken());
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Get the extended token in the database for next use.
		
		
		if ( extendedToken != null ) {
			result.setuToken(extendedToken.getToken());
			RestTemplate updateTokenTemplate = new RestTemplate();
			
			HttpEntity<FbUToken> updatedEntity = new HttpEntity<FbUToken>(result,headers);
			
			updateTokenTemplate.put(userTokenUri, updatedEntity);
			configuration.setOAuthAccessToken(result.getuToken());
			
		}
			

	}

	public String getJwtToken(String userName) {

		String token = "";

		List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));

		if (userName == null || userName.length() < 2 ) {
			userName = "guest";
		}
		// Create new auth token
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userName, "pwd",
				grantedAuths);

		String authorities = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInMilliseconds);

		token = Jwts.builder().setSubject(auth.getName()).claim(AUTHORITIES_KEY, authorities)
				.signWith(SignatureAlgorithm.HS512, secretKey).setExpiration(validity).compact();

		return token;

	}

}
