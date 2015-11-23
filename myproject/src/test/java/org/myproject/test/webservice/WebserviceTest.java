package org.myproject.test.webservice;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;


import org.w3c.dom.NodeList;




public class WebserviceTest {

	private static final String WSSE_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static final QName QNAME_WSSE_USERNAMETOKEN = new QName(WSSE_NS_URI, "UsernameToken");
	private static final QName QNAME_WSSE_USERNAME = new QName(WSSE_NS_URI, "Username");
	private static final QName QNAME_WSSE_PASSWORD = new QName(WSSE_NS_URI, "Password");
	private static final QName QNAME_WSSE_MACADDRS = new QName(WSSE_NS_URI, "MacAddr");
	private static final QName QNAME_WSSE_RESULT = new QName(WSSE_NS_URI, "result");

	private String sessionId = "";
	
	public String getAuthenticationSOAP(String mobilePhone, String passwd) {
		
		String xmlStr = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.myproject.org/\">\n"
					  + "	<soapenv:Header>\n" 
		              + "		<wsse:Security \n"
			          + "			xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
			          + "			mustUnderstand=\"1\">\n"
				      + "			<wsse:UsernameToken\n" 
					  + "					xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n" 
			    	  + "					xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"\n"
			    	  + "					wsu:Id=\"XWSSGID-1287429087296-1526798013\">\n"
		              + "				<wsse:Username>MyProject</wsse:Username>\n"
		              + "				<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">welcome</wsse:Password>\n"
		              + "				<wsse:MacAddr>00-23-ab-00-78</wsse:MacAddr>\n"
			          + "			</wsse:UsernameToken>\n"
		              + "		</wsse:Security>\n"
		              + "	</soapenv:Header>\n"
		              + "	<soapenv:Body>\n"
		              + "		<web:getAuthentication>\n"
		              + "			<!--Optional:-->\n"
		              + "			<mobilePhone>" + mobilePhone + "</mobilePhone>\n"
		              + "			<!--Optional:-->"
		              + "			<passwd>" + passwd + "</passwd>\n"
		              + "		</web:getAuthentication>\n"
		              + "	</soapenv:Body>\n"
		              + "</soapenv:Envelope>\n";
		
		return xmlStr;
	}
	
	
	private SOAPMessage getSoapMessageFromString(String xml) throws SOAPException, IOException {
	    MessageFactory factory = MessageFactory.newInstance();
	    SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
	    return message;
	}
	
	
	private String handleInboundMessage(SOAPMessage message, String tagName) {	
	    SOAPBody body = null;
	    
		try {
			body = message.getSOAPBody();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    NodeList returnList = body.getElementsByTagName(tagName);

	    if (returnList != null) {
		    boolean isSucces = false;
		    
		    for (int k = 0; k < returnList.getLength(); k++) {
		        NodeList innerResultList = returnList.item(k).getChildNodes();
		        
		        for (int l = 0; l < innerResultList.getLength(); l++) {
			        
		            if (innerResultList.item(l).getNodeName()
		                    .equalsIgnoreCase("return")) {
		                isSucces = innerResultList.item(l).getTextContent().length() > 0;
		            }
		        }
		    }
		    
		    if (isSucces) {
		        NodeList list = body.getElementsByTagName(tagName);
	
		        for (int i = 0; i < list.getLength(); i++) {
		            NodeList innerList = list.item(i).getChildNodes();
	
		            for (int j = 0; j < innerList.getLength(); j++) {
//		                System.out.println(innerList.item(j).getNodeName());
//		                System.out.println(innerList.item(j).getTextContent());
		                return innerList.item(j).getTextContent();
		            }
		        }
		    }
	    }
	    
	    return "";
	}
	

    public String soapMessageToString(SOAPMessage message) {
        String result = null;

        if (message != null) {
            ByteArrayOutputStream baos = null;
            try  {
                baos = new ByteArrayOutputStream();
                message.writeTo(baos); 
                result = baos.toString();
            } catch (Exception e) {
            } 
            finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException ioe) {
                    }
                }
            }
        }
        
        return result;
    }   
    
    
	public String getAuthentication(String mobilePhone, String passwd) throws MalformedURLException,	IOException {

		// Code to make a webservice HTTP request
		String responseString = "";
		String outputString = "";
		String wsURL = "http://localhost:8080/myproject/lessonplan/lessonplanws";
		String SOAPAction = "";
		
		URL url = new URL(wsURL);
		URLConnection connection = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection) connection;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		String xmlInput = getAuthenticationSOAP(mobilePhone, passwd);
		
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
			SOAPMessage sm = getSoapMessageFromString(outputString);
//			System.out.println("Message : " + soapMessageToString(sm));
			
			this.sessionId = handleInboundMessage(sm, "ns2:getAuthenticationResponse");
			
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.sessionId;
	}	
	
	
	public static void main(String[] args)  {
		
		WebserviceTest webServiceTest = new WebserviceTest();
		String sessionId;
		
		try {
			sessionId =  webServiceTest.getAuthentication("916655799", "54P6RA%hx0");
			
			System.out.println("sessionId: " + sessionId);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
 
}
