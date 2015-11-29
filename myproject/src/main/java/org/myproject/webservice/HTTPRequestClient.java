package org.myproject.webservice;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.joda.time.DateTime;
import org.springframework.security.crypto.codec.Base64;
import org.w3c.dom.NodeList;

public class HTTPRequestClient {

	private static final String WSSE_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static final QName QNAME_WSSE_USERNAMETOKEN = new QName(WSSE_NS_URI, "UsernameToken");
	private static final QName QNAME_WSSE_USERNAME = new QName(WSSE_NS_URI, "Username");
	private static final QName QNAME_WSSE_PASSWORD = new QName(WSSE_NS_URI, "Password");
	private static final QName QNAME_WSSE_NONCE = new QName(WSSE_NS_URI, "Nonce");
	private static final QName QNAME_WSSE_CREATED = new QName(WSSE_NS_URI, "Created");
	private static final QName QNAME_WSSE_MACADDRS = new QName(WSSE_NS_URI, "MacAddr");
	private static final QName QNAME_WSSE_RESULT = new QName(WSSE_NS_URI, "result");

	private String sessionId = "";


	
	public boolean hostAvailabilityCheck() { 
        String msg = "";
	    URL url;
	    
		try {
			url = new URL( "http://localhost:8080/myproject/ HTTP/1.1" );
		    HttpURLConnection httpConn =  (HttpURLConnection)url.openConnection();
		    httpConn.setInstanceFollowRedirects(false);
		    httpConn.setRequestMethod("HEAD"); 
		    try{
		        httpConn.connect();
	            System.out.println("http://localhost:8080/myproject/ : " + httpConn.getResponseCode());
		        msg = "http://localhost:8080/myproject/ : " + httpConn.getResponseCode();
		    }catch(java.net.ConnectException e){
		         System.out.println("http://localhost:8080/myproject/ : is down ");
		         msg = "http://localhost:8080/myproject/ : is down ";
		    }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		return !msg.contains("is down") && msg.contains("302");
    }
    
    
	public String getAuthentication(String mobilePhone, String passwd) {

		// Code to make a webservice HTTP request
		String responseString = "";
		String outputString = "";
		String wsURL = "http://localhost:8080/myproject/lessonplan/lessonplanws";
		String SOAPAction = "";
		
		try {
			URL url = new URL(wsURL);
			URLConnection connection = url.openConnection();
			
			HttpURLConnection httpConn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			
			XMLRequest xmlRequest = new XMLRequest();
			String xmlInput = xmlRequest.getAuthenticationSOAP(mobilePhone, passwd);
			
			byte[] buffer = new byte[xmlInput.length()];
			buffer = xmlInput.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();
			
			// Set the appropriate HTTP parameters.
			httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction", SOAPAction);
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			OutputStream out = httpConn.getOutputStream();
			
			// Write the content of the request to the outputstream of the HTTP
			// Connection.
			out.write(b);
			out.close();
			
			// Ready with sending the request.
	
			// Read the response.
			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
	
			// Write the SOAP message response to a String.
			while ((responseString = in.readLine()) != null) {
				outputString = outputString + responseString;
			}
			
	
			try {
				// Convert string to SOAPMessage
				SOAPClient soapClient = new SOAPClient();
				SOAPMessage sm = soapClient.getSoapMessageFromString(outputString);
	//			System.out.println("Message : " + soapClient.soapMessageToString(sm));
				
				this.sessionId = soapClient.handleInboundMessage(sm, "sessionId");
				
			} catch (SOAPException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				System.out.println("SOAP ERROR : " + errors.toString());
			}
			
			return this.sessionId;
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return "";
	}	
	
	
	public String setData(String sessionId, String title, String lessonPlan, String lessonDate) {

		// Code to make a webservice HTTP request
		String responseString = "";
		String outputString = "";
		String wsURL = "http://localhost:8080/myproject/lessonplan/lessonplanws";
		String SOAPAction = "";
		
		try {
			URL url = new URL(wsURL);
			URLConnection connection = url.openConnection();
			
			HttpURLConnection httpConn = (HttpURLConnection) connection;
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
	
			XMLRequest xmlRequest = new XMLRequest();
			String xmlInput = xmlRequest.setDataSOAP(sessionId, title, lessonPlan, lessonDate);
			
			byte[] buffer = new byte[xmlInput.length()];
			buffer = xmlInput.getBytes();
			bout.write(buffer);
			byte[] b = bout.toByteArray();
			
			// Set the appropriate HTTP parameters.
			httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
			httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			httpConn.setRequestProperty("SOAPAction", SOAPAction);
			httpConn.setRequestMethod("POST");
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			OutputStream out = httpConn.getOutputStream();
			
			// Write the content of the request to the outputstream of the HTTP
			// Connection.
			out.write(b);
			out.close();
			
			// Ready with sending the request.
	
			// Read the response.
			InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
			BufferedReader in = new BufferedReader(isr);
	
			// Write the SOAP message response to a String.
			while ((responseString = in.readLine()) != null) {
				outputString = outputString + responseString;
			}
			
			String deegreeNum = "-1";
			
			try {
				// Convert string to SOAPMessage
				SOAPClient soapClient = new SOAPClient();
				SOAPMessage sm = soapClient.getSoapMessageFromString(outputString);
	//			System.out.println("Message : " + soapMessageToString(sm));
				
				deegreeNum = soapClient.handleInboundMessage(sm, "degreeNum");
				
			} catch (SOAPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return deegreeNum;
			
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return "";
	}	

	
	public void setFinished(String sessionId) {

		// Code to make a webservice HTTP request
		String responseString = "";
		String outputString = "";
		String wsURL = "http://localhost:8080/myproject/lessonplan/lessonplanws";
		String SOAPAction = "";
		
		try {
		URL url = new URL(wsURL);
		URLConnection connection = url.openConnection();
		
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		XMLRequest xmlRequest = new XMLRequest();
		String xmlInput = xmlRequest.setFinishedSOAP(sessionId);
		
		byte[] buffer = new byte[xmlInput.length()];
		buffer = xmlInput.getBytes();
		bout.write(buffer);
		byte[] b = bout.toByteArray();
		
		// Set the appropriate HTTP parameters.
		httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
		httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpConn.setRequestProperty("SOAPAction", SOAPAction);
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setDoInput(true);
		OutputStream out = httpConn.getOutputStream();
		
		// Write the content of the request to the outputstream of the HTTP
		// Connection.
		out.write(b);
		out.close();
		
		// Ready with sending the request.

		// Read the response.
		InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
		BufferedReader in = new BufferedReader(isr);

		// Write the SOAP message response to a String.
		while ((responseString = in.readLine()) != null) {
			outputString = outputString + responseString;
		}
		

		try {
			// Convert string to SOAPMessage
			SOAPClient soapClient = new SOAPClient();
			SOAPMessage sm = soapClient.getSoapMessageFromString(outputString);
//			System.out.println("Message : " + soapMessageToString(sm));
			
			
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}	

	
}
