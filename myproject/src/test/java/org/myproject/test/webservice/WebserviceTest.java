package org.myproject.test.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;







import javax.xml.soap.SOAPMessage;

import org.myproject.webservice.HTTPRequestClient;
import org.myproject.webservice.SOAPClient;

public class WebserviceTest {

	
	public static void httpRequest () {
		// Problem: cannot handle faultstring from server
		HTTPRequestClient httpRequestClient = new HTTPRequestClient();
		
		String sessionId = "-1";
		
		if (httpRequestClient.hostAvailabilityCheck()) {

			sessionId =  httpRequestClient.getAuthentication("916655799", "54P6RA%hx0");
//				System.out.println("degreeNum: " + webServiceTest.setData(sessionId, "N123 - 3ª - 1ºS 2015/2016", "Teste", "2015-11-08"));
			
			//			webServiceTest.setFinished(sessionId);
		}
		
		System.out.println("sessionId: " + sessionId);

	}
	

	public static void soapRequest () {
		HTTPRequestClient httpRequestClient = new HTTPRequestClient();
		SOAPClient soapClient = new SOAPClient();
		
		String sessionId = "-1";
		
		if (httpRequestClient.hostAvailabilityCheck()) {

			try {
				SOAPMessage soapResponse = null;
				
                soapResponse = soapClient.sendSOAPRequest(soapClient.createSOAPRequestGetAthentication("916655799", "54P6RA%hx0"));
		        
		       	sessionId = soapClient.handleInboundMessage(soapResponse, "sessionId");
				
		       	if (sessionId.contains("Fault")) {
			      	System.out.println("\n");
			        System.out.println(sessionId);
		       	} else {
		       		System.out.println("\n");
		       		System.out.println("sessionId: " + sessionId);
		       		soapResponse = soapClient.sendSOAPRequest(soapClient.createSOAPRequestSetFinished(sessionId));
		       		
		       		sessionId = soapClient.handleInboundMessage(soapResponse, "sessionId");
		       		
		       	}
		       	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 	
		}
		
	}
	
	
	public static void createSOAPRequest() {
		SOAPClient soapClient = new SOAPClient();

/*		
	   xmlns:env="http://www.w3.org/2003/05/soap-envelope"
	   xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
	   xmlns:web="http://webservice.myproject.org/"
	   xmlns:xenc="http://www.w3.org/2001/04/xmlenc#"
	   xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
	   xmlns:xsig="http://www.w3.org/2000/09/xmldsig#"
	   xmlns:enc="http://www.w3.org/2003/05/soap-encoding"
	   xmlns:wsp="http://schemas.xmlsoap.org/ws/2002/12/policy/"
	   xmlns:wst="http://docs.oasis-open.org/ws-sx/ws-trust/200512"
	   env:encodingStyle="http://www.w3.org/2003/05/soap-encoding">
	   <soapenv:Header>
			<wsse:Security 
				xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
					mustUnderstand="1">
				<wsu:Timestamp 
					xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd"
					     wsu:Id="XWSSGID-1287429087296-1526798013">
				    <wsu:Created>2015-11-27T16:47:57.123Z</wsu:Created>
				    <wsu:Expires>2015-11-27T16:49:57.123Z</wsu:Expires>
				</wsu:Timestamp>
				<wsse:UsernameToken
						xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
					<wsse:Username>MyProject</wsse:Username>
					<wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest">MHiyNu2+JXL0XbZshnvYrE8U0Z8=</wsse:Password>
					<wsse:Nonce>bJgTUOzbDbDJ/RkoV8WV5g==</wsse:Nonce>
					<wsse:Created>2015-11-27T21:07:11.970Z</wsse:Created>
					<wsse:MacAddr>00-23-ab-00-78</wsse:MacAddr>
				</wsse:UsernameToken>
			</wsse:Security>
	   </soapenv:Header>
*/
		try {
			SOAPMessage soapMessage = null;
			
			soapMessage = soapClient.createSOAPRequest(false);
            System.out.println(soapClient.prettyPrintXMLAsString(soapClient.soapMessageToString(soapMessage), 4));
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
		
	}
	
	public static void main(String[] args)  {

		createSOAPRequest();
	}
	
}
