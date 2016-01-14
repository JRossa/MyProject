package org.myproject.test.oauth;

import java.util.*;
import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class GoogleExamples {

	private static final String NETWORK_NAME = "Google";
	private static final String AUTHORIZE_URL = "https://www.google.com/accounts/OAuthAuthorizeToken?oauth_token=";
	private static final String PROTECTED_RESOURCE_URL = "https://mail.google.com/mail/feed/atom";
	private static final String SCOPE = "https://mail.google.com/mail"; 

	
	
	public static String getOAuthCode() {

	    String apiKey = MyConstants.GOOGLE_CLIENT_ID;
	    String apiSecret = MyConstants.GOOGLE_CLIENT_SECRET;
	    String callbackUrl = MyConstants.GOOGLE_REDIRECT_URL;
		
		
		OAuthService service = new ServiceBuilder()
	      .provider(GoogleApi.class)
	      .apiKey(apiKey)
	      .apiSecret(apiSecret)
	      .scope(SCOPE)
	      .build();
		
	      Scanner in =new Scanner(System.in);
	      System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
	      System.out.println();

	      //Obtain Request token
	      System.out.println("Fetching request token");
	      Token requestToken = service.getRequestToken();
	      System.out.println("The request token "+requestToken);
	      System.out.println("Authorizing Scribe");
	      System.out.println();
	      System.out.println(AUTHORIZE_URL + requestToken.getToken());
	      System.out.println("And paste the verifier here");
	      System.out.print(">>");
	      Verifier verifier = new Verifier(requestToken.getToken());
	      System.out.println();

	      System.out.println("Trading the Request Token for an Access Token...");
	      Token accessToken = service.getAccessToken(requestToken, verifier);
	      System.out.println("Got the Access Token!");
	      System.out.println("(if your curious it looks like this: " + accessToken + " )");
	      System.out.println();

	      return accessToken.getToken();

	}
}