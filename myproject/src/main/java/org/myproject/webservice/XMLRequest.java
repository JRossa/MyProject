package org.myproject.webservice;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.joda.time.DateTime;
import org.springframework.security.crypto.codec.Base64;

public class XMLRequest {

	// http://weblog.west-wind.com/posts/2012/Nov/24/WCF-WSSecurity-and-WSE-Nonce-Authentication
    public String GetSHA1String() {
    	byte[] nonceBytes = null;
    	SecureRandom random;
    	String sha1String = "";
    	
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
	    	random.setSeed(System.currentTimeMillis()); 
	    	nonceBytes = new byte[16]; 
	    	random.nextBytes(nonceBytes); 
	    	
	   		sha1String = new String(myorg.apache.commons.codec.binary.Base64.encodeBase64(nonceBytes), "UTF-8");

		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	
    	return sha1String;
    }

    
	public String buildPasswordDigest(String userName, String password,	String nonce, String dateTime) {
		MessageDigest sha1;
		String passwordDigest = null;

		try {
			sha1 = MessageDigest.getInstance("SHA-1");
			byte[] hash = MessageDigest.getInstance("SHA-1").digest(password.getBytes("UTF-8"));
			sha1.update(nonce.getBytes("UTF-8"));
			sha1.update(dateTime.getBytes("UTF-8"));
			passwordDigest = new String(Base64.encode(sha1.digest(hash)));
			sha1.reset();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return passwordDigest;
	}
	
	
	private String SOAPHeaderSecurityDigest() {

		String passwordDigest = "";
		
		DateTime dt = new DateTime();
		DateTime dtExpires = dt.plusMinutes(10);
		
		String username = "MyProject";
		String dateTime = dt.toString();
		String nonce = GetSHA1String(); 
		
//		System.out.println("DateTime : " + dateTime + "  " + dtExpires);
		
		passwordDigest = buildPasswordDigest(username, "welcome", nonce, dateTime);
		
//		System.out.println("PasswordDigest : " + passwordDigest);
		
        return "		<wsse:Security \n"
             + "			xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
             + "				mustUnderstand=\"1\">\n"
  	         + "			<wsu:Timestamp \n"
  	         + "				xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"\n"
  	         + "				     wsu:Id=\"XWSSGID-1287429087296-1526798013\" >\n"
   		     + "			    <wsu:Created>" + dt.toString() + "</wsu:Created>\n"
   		     + "			    <wsu:Expires>" + dtExpires.toString() + "</wsu:Expires>\n"
   		     + "			</wsu:Timestamp>\n"	
	         + "			<wsse:UsernameToken\n" 
		     + "					xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" 
             + "				<wsse:Username>" + username + "</wsse:Username>\n"
             + "				<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest\">"
             + passwordDigest + "</wsse:Password>\n"
             + "				<wsse:Nonce>" + nonce + "</wsse:Nonce>\n"
             + "				<wsse:Created>" + dateTime + "</wsse:Created>\n"
             + "				<wsse:MacAddr>00-23-ab-00-78</wsse:MacAddr>\n"
             + "			</wsse:UsernameToken>\n"
             + "		</wsse:Security>\n";

	}
	
	

	private String SOAPHeaderSecurity() {
	
        return "		<wsse:Security \n"
             + "			xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n"
             + "				mustUnderstand=\"1\">\n"
	         + "			<wsse:UsernameToken\n" 
		     + "					xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"\n" 
  	         + "					xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"\n"
  	         + "					wsu:Id=\"XWSSGID-1287429087296-1526798023\">\n"
             + "				<wsse:Username>MyProject</wsse:Username>\n"
             + "				<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">welcome</wsse:Password>\n"
             + "				<wsse:MacAddr>00-23-ab-00-78</wsse:MacAddr>\n"
             + "			</wsse:UsernameToken>\n"
             + "		</wsse:Security>\n";

	}
	
	
	public String getAuthenticationSOAP(String mobilePhone, String passwd) {
		
		String xmlStr = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.myproject.org/\">\n"
					  + "	<soapenv:Header>\n" 
				      + SOAPHeaderSecurityDigest()
		              + "	</soapenv:Header>\n"
		              + "	<soapenv:Body>\n"
		              + "		<web:getAuthentication>\n"
		              + "			<!--Optional:-->\n"
		              + "			<mobilePhone>" + mobilePhone + "</mobilePhone>\n"
		              + "			<!--Optional:-->\n"
		              + "			<passwd>" + passwd + "</passwd>\n"
		              + "		</web:getAuthentication>\n"
		              + "	</soapenv:Body>\n"
		              + "</soapenv:Envelope>\n";
		
		return xmlStr;
	}
	
	
	public String setDataSOAP(String sessionId, String title, String lessonPlan, String lessonDate) {
		
		String xmlStr = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.myproject.org/\">\n"
					  + "	<soapenv:Header>\n" 
				      + SOAPHeaderSecurity()
		              + "	</soapenv:Header>\n"
		              + "	<soapenv:Body>\n"
		              + "		<web:setData>\n"
		              + "			<!--Optional:-->\n"
		              + "			<sessionId>" + sessionId + "</sessionId>\n"
		              + "			<!--Optional:-->\n"
		              + "			<title>" + title + "</title>\n"
		              + "			<!--Optional:-->"
		              + "			<lessonPlan>" + lessonPlan + "</lessonPlan>\n"
		              + "			<!--Optional:-->\n"
		              + "			<lessonDate>" + lessonDate + "</lessonDate>\n"
		              + "		</web:setData>\n"
		              + "	</soapenv:Body>\n"
		              + "</soapenv:Envelope>\n";
		
		return xmlStr;
	}


	public String setFinishedSOAP(String sessionId) {
		
		String xmlStr = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.myproject.org/\">\n"
					  + "	<soapenv:Header>\n" 
				      + SOAPHeaderSecurity()
		              + "	</soapenv:Header>\n"
		              + "	<soapenv:Body>\n"
		              + "		<web:setFinished>\n"
		              + "			<!--Optional:-->\n"
		              + "			<sessionId>" + sessionId + "</sessionId>\n"
		              + "		</web:setFinished>\n"
		              + "	</soapenv:Body>\n"
		              + "</soapenv:Envelope>\n";
		
		return xmlStr;
	}


}
