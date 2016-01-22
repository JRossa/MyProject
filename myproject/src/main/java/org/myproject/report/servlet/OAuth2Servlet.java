package org.myproject.report.servlet;

import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.myproject.test.oauth.MyConstants;

import com.google.common.collect.ImmutableMap;

 
@WebServlet("/oauth2")
public class OAuth2Servlet extends HttpServlet {
	private static final long serialVersionUID = -7398965081284385736L;

	private String stateToken = "myproject";
	
	private String oauthToken;
	
	private final String networkName = "Google";

	private final String protectedResourceUrl = "https://www.googleapis.com/oauth2/v2/userinfo?alt=json";

	private final String clientId = MyConstants.GOOGLE_CLIENT_ID;
	private final String clientSecret = MyConstants.GOOGLE_CLIENT_SECRET;
	private final String redirectUrl = MyConstants.GOOGLE_REDIRECT_URL;
	private final String callbackUrl = "http://localhost:8080/myproject/callback";
	private final String scopeMailUrl = "https://mail.google.com/mail"; 
	private final String scopeMailApisUrl = "https://mail.google.com/ https://www.googleapis.com/auth/userinfo.email";

	private final String scope_1_Url = "https%3A%2F%2Fmail.google.com%2F%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email";

/***	
	URL url = new URL(callbackUrl);
	
	System.out.println("Ant : " + callbackUrl);
	System.out.println("Dep : " + url.toString());

    URIBuilder uri = new URIBuilder();
    uri.setScheme("http")
    .setHost("www.example.com")
    .setPath("/somepage.php")
    .setParameter("username", "Hello Günter")
    .setParameter("p1", "parameter 1");
    System.out.println(uri.toString());
****/
	
	public OAuth2Servlet () {
		super();
	}

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

	  @Override
	  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {

	   // redirect to google for authorization
	   StringBuilder oauthUrl = 
			   new StringBuilder().append("https://accounts.google.com/o/oauth2/auth")
	   .append("?client_id=").append(clientId)          // the client id from the api console registration
	   .append("&response_type=code")
	   .append("&scope=openid%20email")                 // scope is the api permissions we are requesting
	   .append("&redirect_uri=").append(redirectUrl)    // the servlet that google redirects to after authorization
	   .append("&state=").append(stateToken)
	   .append("&access_type=offline")                  // here we are asking to access to user's data while they are not signed in
	   .append("&approval_prompt=force");               // this requires them to verify which account to use, if they are already signed in
	    
	   resp.sendRedirect(oauthUrl.toString());
	  } 


	  protected void doGet_old(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("Hello friend!");

     // redirect to google for authorization
 	   StringBuilder oauthUrl = 
 			   new StringBuilder().append("https://accounts.google.com/o/oauth2/auth")
 			   .append("?response_type=code")
 			   .append("&client_id=").append(clientId)
 			   .append("&redirect_uri=").append(callbackUrl)
 			   .append("&scope=").append(scope_1_Url);

    	if (request.getParameter("code") == null
				|| request.getParameter("state") == null) {
    		System.out.println("Nulo");
    		
        	System.out.println("set Code : " + request.getParameter("code"));
        	System.out.println("State : " + request.getParameter("state"));
   		
    		response.sendRedirect(oauthUrl.toString());
    		return;
    	}

    	System.out.println("Code : " + request.getParameter("code"));
    	System.out.println("State : " + request.getParameter("state"));
    	
    }
	
}

@WebServlet("/signin")
class SigninServlet extends HttpServlet {

	private static final long serialVersionUID = -4042261505755001387L;

	private String stateToken = "myproject";
	
	private String oauthToken;
	
	private final String networkName = "Google";

	private final String protectedResourceUrl = "https://www.googleapis.com/oauth2/v2/userinfo?alt=json";

	private final String clientId = MyConstants.GOOGLE_CLIENT_ID;
	private final String clientSecret = MyConstants.GOOGLE_CLIENT_SECRET;
	private final String redirectUrl = MyConstants.GOOGLE_REDIRECT_URL;
	private final String callbackUrl = "http://localhost:8080/myproject/callback";
	private final String scopeMailUrl = "https://mail.google.com/mail"; 
	private final String scopeMailApisUrl = "https://mail.google.com/ https://www.googleapis.com/auth/userinfo.email";
	private final String scopeOpenId = "openid%20email";
	private final String scope_1_Url = "https%3A%2F%2Fmail.google.com%2F%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email";

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

	@Override
	  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
	    
	   generateStateToken();
	   
	   // redirect to google for authorization
	   StringBuilder oauthUrl = new StringBuilder().append("https://accounts.google.com/o/oauth2/auth")
	   .append("?client_id=").append(clientId)      // the client id from the api console registration
	   .append("&response_type=code")
	   .append("&scope=").append(scopeOpenId)       // scope is the api permissions we are requesting
	   .append("&redirect_uri=").append(callbackUrl)// the servlet that google redirects to after authorization
	   .append("&state=").append(stateToken)
	   .append("&access_type=offline")              // here we are asking to access to user's data while they are not signed in
	   .append("&approval_prompt=force");           // this requires them to verify which account to use, if they are already signed in
	    
	   resp.sendRedirect(oauthUrl.toString());
	  } 
 }



@WebServlet("/callback")
class CallbackServlet extends HttpServlet {

	private static final long serialVersionUID = -8895416649840223536L;

	private String stateToken = "myproject";
	
	private String oauthToken;
	
	private final String networkName = "Google";

	private final String protectedResourceUrl = "https://www.googleapis.com/oauth2/v2/userinfo?alt=json";

	private final String clientId = MyConstants.GOOGLE_CLIENT_ID;
	private final String clientSecret = MyConstants.GOOGLE_CLIENT_SECRET;
	private final String redirectUrl = MyConstants.GOOGLE_REDIRECT_URL;
	private final String callbackUrl = "http://localhost:8080/myproject/callback";
	private final String scopeMailUrl = "https://mail.google.com/mail"; 
	private final String scopeMailApisUrl = "https://mail.google.com/ https://www.googleapis.com/auth/userinfo.email";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// google redirects with
		// http://localhost:8089/callback?state=this_can_be_anything_to_help_correlate_the_response%3Dlike_session_id&code=4/ygE-kCdJ_pgwb1mKZq3uaTEWLUBd.slJWq1jM9mcUEnp6UAPFm0F2NQjrgwI&authuser=0&prompt=consent&session_state=a3d1eb134189705e9acf2f573325e6f30dd30ee4..d62c

		// if the user denied access, we get back an error, ex
		// error=access_denied&state=session%3Dpotatoes

		if (req.getParameter("error") != null) {
			resp.getWriter().println(req.getParameter("error"));
			return;
		}

		// google returns a code that can be exchanged for a access token
		String code = req.getParameter("code");
		String state = req.getParameter("state");

		req.getSession().setAttribute("code", code);
		req.getSession().setAttribute("state", state);
		
		// get the access token by post to Google
		String body = post("https://accounts.google.com/o/oauth2/token",
				ImmutableMap.<String, String> builder()
				.put("code", code)
				.put("client_id", clientId)
				.put("client_secret", clientSecret)
				.put("redirect_uri", callbackUrl)
				.put("grant_type", "authorization_code").build());

		// ex. returns
		// {
		// "access_token":
		// "ya29.AHES6ZQS-BsKiPxdU_iKChTsaGCYZGcuqhm_A5bef8ksNoU",
		// "token_type": "Bearer",
		// "expires_in": 3600,
		// "id_token":
		// "eyJhbGciOiJSUzI1NiIsImtpZCI6IjA5ZmE5NmFjZWNkOGQyZWRjZmFiMjk0NDRhOTgyN2UwZmFiODlhYTYifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwiZW1haWxfdmVyaWZpZWQiOiJ0cnVlIiwiZW1haWwiOiJhbmRyZXcucmFwcEBnbWFpbC5jb20iLCJhdWQiOiI1MDgxNzA4MjE1MDIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdF9oYXNoIjoieUpVTFp3UjVDX2ZmWmozWkNublJvZyIsInN1YiI6IjExODM4NTYyMDEzNDczMjQzMTYzOSIsImF6cCI6IjUwODE3MDgyMTUwMi5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImlhdCI6MTM4Mjc0MjAzNSwiZXhwIjoxMzgyNzQ1OTM1fQ.Va3kePMh1FlhT1QBdLGgjuaiI3pM9xv9zWGMA9cbbzdr6Tkdy9E-8kHqrFg7cRiQkKt4OKp3M9H60Acw_H15sV6MiOah4vhJcxt0l4-08-A84inI4rsnFn5hp8b-dJKVyxw1Dj1tocgwnYI03czUV3cVqt9wptG34vTEcV3dsU8",
		// "refresh_token": "1/Hc1oTSLuw7NMc3qSQMTNqN6MlmgVafc78IZaGhwYS-o"
		// }

		System.out.println("Pass2  body  " + body);

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
		req.getSession().setAttribute("access_token", accessToken);

		// get some info about the user with the access token
		String json = get(new StringBuilder("https://www.googleapis.com/oauth2/v1/userinfo?access_token=")
				.append(accessToken).toString());

		// now we could store the email address in session

		// return the json of the user's basic info
		resp.getWriter().println(json);
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
}
