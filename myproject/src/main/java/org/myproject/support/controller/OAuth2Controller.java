package org.myproject.support.controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.myproject.model.entities.LogSession;
import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.LogSessionRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.support.user.UserMBean;
import org.myproject.test.oauth.MyConstants;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.collect.ImmutableMap;


//  https://docs.spring.io/spring-security/site/docs/3.1.6.RELEASE/reference/technical-overview.html


@Controller
@RequestMapping(value = { "" })
public class OAuth2Controller {

    private static final Logger logger = Logger.getLogger(OAuth2Controller.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private LogSessionRepository sessionRepository;
    
    private UserMBean userMBean;

	/**
	 * Please provide a value for the CLIENT_ID constant before proceeding, set this up at https://code.google.com/apis/console/
	 */
	private static final String CLIENT_ID = MyConstants.GOOGLE_CLIENT_ID;
	/**
	 * Please provide a value for the CLIENT_SECRET constant before proceeding, set this up at https://code.google.com/apis/console/
	 */
	private static final String CLIENT_SECRET = MyConstants.GOOGLE_CLIENT_SECRET;

	/**
	 * Callback URI that google will redirect to after successful authentication
	 */
//	private static final String CALLBACK_URI = "http://localhost:8080/OAuth2v1/index.jsp";
	private static final String CALLBACK_URI = MyConstants.GOOGLE_REDIRECT_URL;
	
	// start google authentication constants
	private static final Collection<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email".split(";"));
	private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	// end google authentication constants
	
	private String stateToken;
	
	private GoogleAuthorizationCodeFlow flow;

	private static AuthenticationManager am = new SampleAuthenticationManager();

	/**
	 * Generates a secure state token 
	 */
	private void generateStateToken(){
		
		SecureRandom sr1 = new SecureRandom();
		
		stateToken = "google;"+sr1.nextInt();
		
	}
	
	/**
	 * Accessor for state token
	 */
	public String getStateToken(){
		return stateToken;
	}

	/**
	 * Expects an Authentication Code, and makes an authenticated request for the user's profile information
	 * @return JSON formatted user profile information
	 * @param authCode authentication code provided by google
	 */
	public String getUserInfoJson(final String authCode) throws IOException {

		final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(CALLBACK_URI).execute();
		final Credential credential = flow.createAndStoreCredential(response, null);
		final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
		// Make an authenticated request
		final GenericUrl url = new GenericUrl(USER_INFO_URL);
		final HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");
		final String jsonIdentity = request.execute().parseAsString();

		return jsonIdentity;

	}

	
	// makes a GET request to url and returns body as a string
	public String get(String url) throws ClientProtocolException, IOException {
		return execute(new HttpGet(url));
	}

	// makes a POST request to url with form parameters and returns body as a
	// string
	public String post(String url, Map<String, String> formParameters) throws ClientProtocolException, IOException {
		HttpPost request = new HttpPost(url);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		for (String key : formParameters.keySet()) {
			nvps.add(new BasicNameValuePair(key, formParameters.get(key)));
		}

		request.setEntity(new UrlEncodedFormEntity(nvps));
		
		return execute(request);
	}

	// makes request and checks response code for 200
	private String execute(HttpRequestBase request) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
     
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Expected 200 but got " + response.getStatusLine().getStatusCode() + ", with body " + body);
		}

		return body;
	}

	
    @RequestMapping("/oauth2callback")
    public void oauth2Code(HttpServletRequest request, HttpServletResponse response)
    		                                     throws ServletException, IOException{
    	 
    	System.out.println("Code  :  " + request.getParameter("code"));
    	System.out.println("State  :  " + request.getParameter("state"));
    	System.out.println("Code  :  " + request.getParameter("access_token"));

		// if the user denied access, we get back an error, ex
		// error=access_denied&state=session%3Dpotatoes

		if (request.getParameter("error") != null) {
			response.getWriter().println(request.getParameter("error"));
			return;
		}
		
		// google returns a code that can be exchanged for a access token
		String code = request.getParameter("code");
		String state = request.getParameter("state");

		request.getSession().setAttribute("code", code);
		request.getSession().setAttribute("state", state);

		// get the access token by post to Google
		String body = post("https://accounts.google.com/o/oauth2/token",
				ImmutableMap.<String, String> builder()
				.put("code", code)
				.put("client_id", CLIENT_ID)
				.put("client_secret", CLIENT_SECRET)
				.put("redirect_uri", CALLBACK_URI)
				.put("grant_type", "authorization_code").build());
		
		System.out.println("- Access Token -\n" + body);

		JSONObject jsonObject = null;

		// get the access token from json and request info from Google
		try {
			jsonObject = (JSONObject) new JSONParser().parse(body);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse json " + body);
		}

		// google tokens expire after an hour, but since we requested offline
		// access we can get a new token without user involvement via the
		// refresh token
		String accessToken = (String) jsonObject.get("access_token");

		// you may want to store the access token in session
		request.getSession().setAttribute("access_token", accessToken);

		// get some info about the user with the access token
		String json = get(new StringBuilder("https://www.googleapis.com/oauth2/v1/userinfo?access_token=")
				.append(accessToken).toString());

		// now we could store the email address in session
		System.out.println("- User Data -\n" + json);

		// return the json of the user's basic info
		response.getWriter().println(json);

        String adminTargetUrl = "/myproject/schedule/admin";

		try {
			
				// TODO - FindUserByEmail
			String username = "admin";
			String password = "";
			String rolename = "ROLE_ADMIN";
			String userId = "18";
			
			Authentication arequest =
					new UsernamePasswordAuthenticationToken(username, password,
					        AuthorityUtils.commaSeparatedStringToAuthorityList(rolename));
			
			Authentication result = am.authenticate(arequest);
			
			SecurityContextHolder.getContext().setAuthentication(result);
			
			String logNameMessage = getUserFullName(username, rolename);
			String lastLogMessage = getLastLogin(username);
			
			request.getSession().setAttribute("username", username);
			request.getSession().setAttribute("rolename", rolename);
			request.getSession().setAttribute("userId", userId);
			request.getSession().setAttribute("changepass", 0);
			
			LogUser user = this.userRepository.findByUserName(username);
			
			user.setAttempts(0);
			
			this.userRepository.saveAndFlush(user);
			
			LogSession session = new LogSession();
			
			session.setUser(user);
			session.setActive(true);
			session.setStartDate(new Date());
			session.setEndDate(new Date());
			
			this.sessionRepository.saveAndFlush(session);
			
			request.getSession().setAttribute("logNameMessage", logNameMessage);
			
			if (lastLogMessage != null)
			  request.getSession().setAttribute("lastLogMessage", lastLogMessage);
		    
		    System.out.println("Successfully authenticated. Security context contains: " +
		              SecurityContextHolder.getContext().getAuthentication());
		 
		} catch(AuthenticationException e) {
		    System.out.println("Authentication failed: " + e.getMessage());
		}
		
        response.sendRedirect(adminTargetUrl);
    }   

    
    
    @RequestMapping("/oauth2request")
    public void oauth2Request(HttpServletRequest request, HttpServletResponse response) {

    	System.out.println("request Code  :  " + request.getParameter("code"));
    	System.out.println("request State  :  " + request.getParameter("state"));
    	System.out.println("request Code  :  " + request.getParameter("access_token"));

		try {
			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
					JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).build();
			
			generateStateToken();
	
			final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
			
			String oauthUrl = url.setRedirectUri(CALLBACK_URI).setState(stateToken).build(); 
			System.out.println(oauthUrl);

			response.sendRedirect(oauthUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    private String getLastLogin(String username) {
        String lastlogin = "";

        LogUser user = this.userRepository.findByUserName(username);

        if (user != null) {

            if (user.getLastLoginDate() == null) {
                // Para resolver o primeiro acesso colocar CreationDate +
                // Modification Date
                if (user.getCreationDate() != null) {
                    if (user.getModificationDate() != null) {
                        logger.info("Last Login " + user.getModificationDate());
                        lastlogin = user.getModificationDate().toString();
                    }
                } else {
                    lastlogin = new Date().toString();
                    user.setModificationDate(new Date());
                    user.setCreationDate(new Date());
                }
    
                user.setLastLoginDate(user.getModificationDate());
                user.setModificationDate(new Date());
                user.setModificationUser(username);
            } else {
                // Para quando o utilizador está corretamente inserido
                logger.info("Last Login " + user.getLastLoginDate());
                lastlogin = user.getLastLoginDate().toString();
                user.setLastLoginDate(new Date());
            }
                
            this.userRepository.saveAndFlush(user);

        } else {
            logger.error("User not found !!!");
        }

        return lastlogin;
    }

    
    private String getUserFullName(String username, String rolename) {
        String userFullName = "";

        LogUser user = this.userRepository.findByUserName(username);

        if (user != null) {
            if (rolename.equals("ROLE_USER_T") || rolename.equals("ROLE_ADMIN")) {
                logger.debug("Name " + user.getTeacher().getFullName());
                userFullName = user.getTeacher().getFullName();
            }
        } else {
            logger.error("User not found !!!");
        }

        return userFullName;
    }
    
    // Exemplo
    @RequestMapping(value = "/oauth2staticPage", method = RequestMethod.GET)
    public String redirectOAuth2() {
      
       return "redirect:/pages/final.htm";
    }
 
}

//https://docs.spring.io/spring-security/site/docs/3.1.6.RELEASE/reference/technical-overview.html
class SampleAuthenticationManager implements AuthenticationManager {
	  static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

	  static {
	    AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	  }
	  
	  public Authentication authenticate(Authentication auth) throws AuthenticationException {
		    	
/*		      
	  return new UsernamePasswordAuthenticationToken(auth.getName(),
		                            auth.getCredentials(), AUTHORITIES);
*/
		  return new UsernamePasswordAuthenticationToken(auth.getName(),
				                         auth.getCredentials(), auth.getAuthorities());
	  }
	  
	  
}

