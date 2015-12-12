package org.myproject.webservice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.joda.time.DateTime;
import org.myproject.dao.LessonPlanUser;


public class SOAPClient {

	
	
	public SOAPMessage getSoapMessageFromString(String xml) throws SOAPException, IOException {
	    MessageFactory factory = MessageFactory.newInstance();
	    SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
	    return message;
	}
	
	public String handleInboundMessage(SOAPMessage soapMessage, String tagName) {	
	    String value = "";
        Boolean faultString = false;
        
	    try {
	    	System.out.print("Inbound SOAP Message:\n");
//	    	soapMessage.writeTo(System.out);
	    	System.out.println(prettyPrintXMLAsString(soapMessageToString(soapMessage), 4));
//	    	System.out.println(prettyPrintXml(soapMessageToString(soapMessage)));

	    	
	    	SOAPPart soapPart = soapMessage.getSOAPPart();
	        SOAPEnvelope envelop = soapPart.getEnvelope();
	        SOAPHeader header = envelop.getHeader();
	        SOAPBody body = envelop.getBody();

	        System.out.println("\nBody  : " +  body.getElementName().getLocalName());
	        
	        Iterator<?> ii = body.getChildElements();
	        while (ii.hasNext()) {
                
	            SOAPElement e = (SOAPElement) ii.next();
	            System.out.println("Child : " +  e.getElementName().getLocalName());
	            faultString = e.getElementName().getLocalName().equals("Fault");
	            
	            Iterator<?> kk = e.getChildElements();

	            while (kk.hasNext()) {

	                SOAPElement ee = (SOAPElement)kk.next();
	                String name = ee.getElementName().getLocalName();

	                if (!faultString) {
		                if (ee.getElementName().getLocalName().equals(tagName)) {
		                	value = ee.getValue();
		                }
	                } else {
	                	if (ee.getValue().equals("soap:Server")) {
	                		value = "Fault: " + ee.getValue() + " - ";
	                	} else {
	                		value = value + ee.getValue();
	                	}
	                }
	                
	                if( name != null ) {
	                    System.out.println("	name = " + name + "    value " + ee.getValue() + "  fault : " + faultString);
	                }
	            }
	        }

	    } catch(Exception e) {
	        e.printStackTrace(); 
	    }

	    return value;
	}

	private static final QName QNAME_TITLE = new QName("title");
	private static final QName QNAME_PLACE = new QName("place");
	private static final QName QNAME_START_TIME = new QName("startTime");
	private static final QName QNAME_END_TIME = new QName("endTime");
	
	public ArrayList<LessonPlanUser> handleArrayListInboundMessage(SOAPMessage soapMessage, String tagName) {	
	    String value = "";
        Boolean faultString = false;
        
        ArrayList<LessonPlanUser> lessonPlanUser = new ArrayList<LessonPlanUser>();
        
	    try {
	    	System.out.print("Inbound SOAP Message:\n");
//	    	soapMessage.writeTo(System.out);
	    	System.out.println(prettyPrintXMLAsString(soapMessageToString(soapMessage), 4));
//	    	System.out.println(prettyPrintXml(soapMessageToString(soapMessage)));

	    	
	    	SOAPPart soapPart = soapMessage.getSOAPPart();
	        SOAPEnvelope envelop = soapPart.getEnvelope();
	        SOAPHeader header = envelop.getHeader();
	        SOAPBody body = envelop.getBody();

	        System.out.println("\nBody  : " +  body.getElementName().getLocalName());
	        
	        Iterator<?> ii = body.getChildElements();
	        while (ii.hasNext()) {
                
	            SOAPElement e = (SOAPElement) ii.next();
	            System.out.println("Child : " +  e.getElementName().getLocalName());
	            faultString = e.getElementName().getLocalName().equals("Fault");
	            
	            Iterator<?> kk = e.getChildElements();

	            while (kk.hasNext()) {

	                SOAPElement ee = (SOAPElement)kk.next();
	                String name = ee.getElementName().getLocalName();

	                if (!faultString) {
    	            	if (ee.getElementName().getLocalName().equals(tagName)) {
    	            	
    	                    System.out.println("IN child	title = " + getFirstChildElementValue(ee, QNAME_TITLE));
    	                    System.out.println("IN child	name  = " + getFirstChildElementValue(ee, QNAME_PLACE));
    	                    System.out.println("IN child	start = " + getFirstChildElementValue(ee, QNAME_START_TIME));
    	                    System.out.println("IN child	end   = " + getFirstChildElementValue(ee, QNAME_END_TIME));
	    	            	
		    	            LessonPlanUser usr = new LessonPlanUser(getFirstChildElementValue(ee, QNAME_TITLE),
		    	            		                                getFirstChildElementValue(ee, QNAME_PLACE),
		    	            		                                getFirstChildElementValue(ee, QNAME_START_TIME),
		    	            		                                getFirstChildElementValue(ee, QNAME_END_TIME));
		    	            lessonPlanUser.add(usr);
		    	            
		                }
	                } else {
	                	if (ee.getValue().equals("soap:Server")) {
	                		value = "Fault: " + ee.getValue() + " - ";
	                	} else {
	                		value = value + ee.getValue();
	                	}
	                }
	                
	                if( name != null ) {
	                    System.out.println("	name = " + name + "    value " + ee.getValue() + "  fault : " + faultString);
	                }
	            }
	        }

	    } catch(Exception e) {
	        e.printStackTrace(); 
	    }

	    return lessonPlanUser;
	}
	

    private String getFirstChildElementValue(SOAPElement soapElement, QName qNameToFind) {
        String value = null;
        Iterator<?> it = soapElement.getChildElements(qNameToFind);
         
        while (it.hasNext()) {
            SOAPElement element = (SOAPElement) it.next(); //use first
            value = element.getValue();
         }
         
         return value;
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
            } finally {
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


	public String prettyPrintXml(String xml) {

	    final StringWriter sw;

	    try {
	        final OutputFormat format = OutputFormat.createPrettyPrint();
	        final org.dom4j.Document document = DocumentHelper.parseText(xml);
	        sw = new StringWriter();
	        final XMLWriter writer = new XMLWriter(sw, format);
	        writer.write(document);
	    }
	    catch (Exception e) {
	        throw new RuntimeException("Error pretty printing xml:\n" + xml, e);
	    }
	    return sw.toString();
	}

	
	public String prettyPrintXMLAsString(String xmlString, int tabSize) {
	    /* Remove new lines */
	    final String LINE_BREAK = "\n";
	    final String TAB = "\t";
	    
	    xmlString = xmlString.replaceAll(LINE_BREAK, "");
	    xmlString.replaceAll(TAB, "");
	    
	    StringBuffer prettyPrintXml = new StringBuffer();
	    /* Group the xml tags */
	    Pattern pattern = Pattern.compile("(<[^/][^>]+>)?([^<]*)(</[^>]+>)?(<[^/][^>]+/>)?");
	    Matcher matcher = pattern.matcher(xmlString);
	    int tabCount = 0;
	    while (matcher.find()) {
	        String str1 = (null == matcher.group(1) || "null".equals(matcher.group())) ? "" : matcher.group(1);
	        String str2 = (null == matcher.group(2) || "null".equals(matcher.group())) ? "" : matcher.group(2);
	        String str3 = (null == matcher.group(3) || "null".equals(matcher.group())) ? "" : matcher.group(3);
	        String str4 = (null == matcher.group(4) || "null".equals(matcher.group())) ? "" : matcher.group(4);

	        if (matcher.group() != null && !matcher.group().trim().equals("")) {
	            printTabs(tabCount, prettyPrintXml, TAB);
	            if (!str1.equals("") && str3.equals("")) {
	                ++tabCount;
	            }
	            if (str1.equals("") && !str3.equals("")) {
	                --tabCount;
	                prettyPrintXml.deleteCharAt(prettyPrintXml.length() - 1);
	            }

	            prettyPrintXml.append(str1);
	            prettyPrintXml.append(str2);
	            prettyPrintXml.append(str3);
	            if (!str4.equals("")) {
	                prettyPrintXml.append(LINE_BREAK);
	                printTabs(tabCount, prettyPrintXml, TAB);
	                prettyPrintXml.append(str4);
	            }
	            prettyPrintXml.append(LINE_BREAK);
	        }
	    }
	   
	    String convertTabs = prettyPrintXml.toString();

	    String tabSpaces = "";
	    for (int i = 0; i < tabSize; i++) {
	    	tabSpaces = tabSpaces.concat(" ");
	    }
	    
	    return convertTabs.replace(TAB, tabSpaces);
	}

	
	private void printTabs(int count, StringBuffer stringBuffer, String tabChar) {
	    for (int i = 0; i < count; i++) {
	        stringBuffer.append(tabChar);
	    }
	}


	public SOAPMessage sendSOAPRequest(SOAPMessage soapRequest) {
        // Create SOAP Connection
		try {
	        SOAPConnectionFactory soapConnectionFactory;
			soapConnectionFactory = SOAPConnectionFactory.newInstance();
	        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

	        // Send SOAP Message to SOAP Server
	        String url = "http://localhost:8080/myproject/lessonplan/lessonplanws";
	        SOAPMessage soapResponse = soapConnection.call(soapRequest, url);

	        // print SOAP Response
	        System.out.print("Response SOAP Message:\n");
//	        soapResponse.writeTo(System.out);
//	        System.out.println(prettyPrintXMLAsString(soapMessageToString(soapResponse), 4));
	        soapConnection.close();
	        
	        return  soapResponse;
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
    }

	
    public SOAPMessage createSOAPRequestGetAthentication(String mobilePhone, String passwd) throws Exception {
        MessageFactory factory=MessageFactory.newInstance();
        XMLRequest xmlRequest = new XMLRequest();
        
        String msg = xmlRequest.getAuthenticationSOAP(mobilePhone, passwd);
        
        SOAPMessage soapMessage = factory.createMessage(null,new ByteArrayInputStream(msg.getBytes()));
        
        /* Print the request message */
        System.out.print("Request SOAP Message:\n");
//        soapMessage.writeTo(System.out);
//        System.out.println(prettyPrintXMLAsString(soapMessageToString(soapMessage), 4));
        System.out.println();
        
        return soapMessage;
    }
    
 

    public SOAPMessage createSOAPRequestGetData(String sessionId) throws Exception {
    	MessageFactory factory=MessageFactory.newInstance();
    	XMLRequest xmlRequest = new XMLRequest();

    	String msg = xmlRequest.getDataSOAP(sessionId);

    	SOAPMessage soapMessage = factory.createMessage(null,new ByteArrayInputStream(msg.getBytes()));

    	/* Print the request message */
    	System.out.print("Request SOAP Message:\n");
    	//soapMessage.writeTo(System.out);
    	//System.out.println(prettyPrintXMLAsString(soapMessageToString(soapMessage), 4));
    	System.out.println();

    	return soapMessage;
    }



    public SOAPMessage createSOAPRequestSetData(String sessionId, String title, 
    		                                    String lessonPlan, String lessonDate) throws Exception {
        MessageFactory factory=MessageFactory.newInstance();
        XMLRequest xmlRequest = new XMLRequest();
        
        String msg = xmlRequest.setDataSOAP(sessionId, title, lessonPlan, lessonDate);
        
        SOAPMessage soapMessage = factory.createMessage(null,new ByteArrayInputStream(msg.getBytes()));
        
        /* Print the request message */
        System.out.print("Request SOAP Message:\n");
//        soapMessage.writeTo(System.out);
//        System.out.println(prettyPrintXMLAsString(soapMessageToString(soapMessage), 4));
        System.out.println();
        
        return soapMessage;
    }

    
    public SOAPMessage createSOAPRequestSetFinished(String sessionId) throws Exception {
        MessageFactory factory=MessageFactory.newInstance();
        XMLRequest xmlRequest = new XMLRequest();
        
        String msg = xmlRequest.setFinishedSOAP(sessionId);
        
        SOAPMessage soapMessage = factory.createMessage(null,new ByteArrayInputStream(msg.getBytes()));
        
        /* Print the request message */
        System.out.print("Request SOAP Message:\n");
//        soapMessage.writeTo(System.out);
//        System.out.println(prettyPrintXMLAsString(soapMessageToString(soapMessage), 4));
        System.out.println();
        
        return soapMessage;
    }
  
    public static final String WSSE_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
    public static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
    public static final String PASSWORD_DIGEST_TYPE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordDigest";
    public static final String WSU_DIGEST_ID = "XWSSGID-1287429087296-1526798013";
    public static final String PASSWORD_TEXT_TYPE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";
    public static final String WSU_TEXT_ID = "XWSSGID-1287429087296-1526798023";
    public static final String WSSE_SECURITY_LNAME = "Security";
    public static final String WSSE_NS_PREFIX = "wsse";
    public static final String WSU_NS_PREFIX = "wsu";
    
    
    public SOAPMessage createSOAPRequest(Boolean Digest) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
 
        boolean mustUnderstand = false;

        XMLRequest xmlRequest = new XMLRequest();
		DateTime dtCreate = new DateTime();
		DateTime dtExpires = dtCreate.plusMinutes(10);
		
		String username = "MyProject";
		String nonce = xmlRequest.GetSHA1String(); 
       
		String passwordDigest = xmlRequest.buildPasswordDigest(username, "welcome", nonce, dtCreate.toString());
		
        String serverURI = "http://localhost:8080/myproject/lessonplan/lessonplanws";
        
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
        envelope.addNamespaceDeclaration("env", "http://schemas.xmlsoap.org/soap/envelope/");
       envelope.addNamespaceDeclaration("web", "http://webservice.myproject.org/");
//        envelope.setEncodingStyle("http://www.w3.org/2003/05/soap-encoding");
        Name encodingStyleName = envelope.createName("env:encodingStyle");
        envelope.addAttribute(encodingStyleName, "http://www.w3.org/2003/05/soap-encoding");

        /*
        Constructed SOAP Request Message:
        <SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:example="http://ws.cdyne.com/">
            <SOAP-ENV:Header/>
            <SOAP-ENV:Body>
                <example:VerifyEmail>
                    <example:email>mutantninja@gmail.com</example:email>
                    <example:LicenseKey>123</example:LicenseKey>
                </example:VerifyEmail>
            </SOAP-ENV:Body>
        </SOAP-ENV:Envelope>
         */

        // SOAP Header
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        SOAPHeader soapHeader = envelope.getHeader();
        Name securityName = soapFactory.createName(WSSE_SECURITY_LNAME, WSSE_NS_PREFIX, WSSE_NS);
        SOAPHeaderElement securityElem = soapHeader.addHeaderElement(securityName);
//        securityElem.setMustUnderstand(mustUnderstand);
		Name mustundertandName = envelope.createName("mustunderstand");
		securityElem.addAttribute(mustundertandName, "1");

		// Timestamp
        Name timestampName = soapFactory.createName("Timestamp", WSU_NS_PREFIX, WSU_NS);
        SOAPElement timestampTokenMsgElem = soapFactory.createElement(timestampName);
        Name idName = envelope.createName("wsu:Id");
        if (Digest) {
        	timestampTokenMsgElem.addAttribute(idName, WSU_DIGEST_ID);
        } else {
        	timestampTokenMsgElem.addAttribute(idName, WSU_TEXT_ID);
        }
        
        Name createdTSName = soapFactory.createName("Created", WSU_NS_PREFIX, WSU_NS);
        SOAPElement createdTSMsgElem = soapFactory.createElement(createdTSName);
        createdTSMsgElem.addTextNode(dtCreate.toString());
        timestampTokenMsgElem.addChildElement(createdTSMsgElem);

        Name expiresTSName = soapFactory.createName("Expires", WSU_NS_PREFIX, WSU_NS);
        SOAPElement expiresTSMsgElem = soapFactory.createElement(expiresTSName);
        expiresTSMsgElem.addTextNode(dtExpires.toString());
        timestampTokenMsgElem.addChildElement(expiresTSMsgElem);
        
        // UsernameToken
        Name usernameTokenName = soapFactory.createName("UsernameToken", WSSE_NS_PREFIX, WSSE_NS);
        SOAPElement usernameTokenMsgElem = soapFactory.createElement(usernameTokenName);

        Name usernameName = soapFactory.createName("Username", WSSE_NS_PREFIX, WSSE_NS);
        SOAPElement usernameMsgElem = soapFactory.createElement(usernameName);
        usernameMsgElem.addTextNode(username);
        usernameTokenMsgElem.addChildElement(usernameMsgElem);

        if (Digest) {
            Name passwordName = soapFactory.createName("Type", WSSE_NS_PREFIX, WSSE_NS);
            SOAPElement passwordMsgElem = soapFactory.createElement("Password", WSSE_NS_PREFIX, WSSE_NS);
            passwordMsgElem.addAttribute(passwordName, PASSWORD_DIGEST_TYPE);
            passwordMsgElem.addTextNode(passwordDigest);
            usernameTokenMsgElem.addChildElement(passwordMsgElem);
        } else {
	        Name passwordName = soapFactory.createName("Type", WSSE_NS_PREFIX, WSSE_NS);
	        SOAPElement passwordMsgElem = soapFactory.createElement("Password", WSSE_NS_PREFIX, WSSE_NS);
	        passwordMsgElem.addAttribute(passwordName, PASSWORD_TEXT_TYPE);
	        passwordMsgElem.addTextNode("welcome");
	        usernameTokenMsgElem.addChildElement(passwordMsgElem);
        }
        
        Name nonceName = soapFactory.createName("Nonce", WSSE_NS_PREFIX, WSSE_NS);
        SOAPElement nonceMsgElem = soapFactory.createElement(nonceName);
        nonceMsgElem.addTextNode(nonce);
        usernameTokenMsgElem.addChildElement(nonceMsgElem);

        Name createdUTName = soapFactory.createName("Created", WSSE_NS_PREFIX, WSSE_NS);
        SOAPElement createdUTMsgElem = soapFactory.createElement(createdUTName);
        createdUTMsgElem.addTextNode(dtCreate.toString());
        usernameTokenMsgElem.addChildElement(createdUTMsgElem);

        securityElem.addChildElement(timestampTokenMsgElem);
        securityElem.addChildElement(usernameTokenMsgElem);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
       
        SOAPElement soapBodyElem = soapBody.addChildElement("getAuthentication", "web");
        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("mobilePhone");
        soapBodyElem1.addTextNode("916655799");

        SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("passwd");
        soapBodyElem2.addTextNode("54P6RA%hx0");

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", serverURI  + "");

        soapMessage.saveChanges();

        /* Print the request message */
        System.out.print("Request SOAP Message:\n");
//        soapMessage.writeTo(System.out);
//        System.out.println();

        return soapMessage;
    }

    
    public String getAuthentication(String mobilePhone, String passwd) {
    	
		try {
			SOAPMessage soapResponse = sendSOAPRequest(createSOAPRequestGetAthentication(mobilePhone, passwd));
			return handleInboundMessage(soapResponse, "sessionId");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
       	return "";
    }
    
  
	public ArrayList<LessonPlanUser> getData(String sessionId) {

		try {
			SOAPMessage soapResponse = sendSOAPRequest(createSOAPRequestGetData(sessionId));
			return handleArrayListInboundMessage(soapResponse, "LessonPlanUser");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
       	return null;
	}

	
	public String setData(String sessionId, String title, String lessonPlan, String lessonDate) {

		try {
			SOAPMessage soapResponse = sendSOAPRequest(createSOAPRequestSetData(sessionId, title, lessonPlan, lessonDate));
			return handleInboundMessage(soapResponse, "degreeNum");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
       	return "";
	}
	
	
    public void setFinished(String sessionId) {
 
		try {
			sendSOAPRequest(createSOAPRequestSetFinished(sessionId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}