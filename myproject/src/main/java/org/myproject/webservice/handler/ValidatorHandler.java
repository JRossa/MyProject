package org.myproject.webservice.handler;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.security.crypto.codec.Base64;

public class ValidatorHandler implements SOAPHandler<SOAPMessageContext> {

	private static final Logger LOGGER = Logger.getLogger(ValidatorHandler.class);

	private static String typePassword = "";
	private static String wsuId = null;
	private static String wsuCreated  = null;
	private static String wsuExpires  = null;
	
	private static final String WSSE_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static final String WSU_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
	
	private static final QName QNAME_WSSE_USERNAMETOKEN = new QName(WSSE_NS_URI, "UsernameToken");
	private static final QName QNAME_WSSE_USERNAME = new QName(WSSE_NS_URI, "Username");
	private static final QName QNAME_WSSE_PASSWORD = new QName(WSSE_NS_URI, "Password");
	private static final QName QNAME_WSSE_NONCE = new QName(WSSE_NS_URI, "Nonce");
	private static final QName QNAME_WSSE_CREATED = new QName(WSSE_NS_URI, "Created");
	private static final QName QNAME_WSSE_MACADDRS = new QName(WSSE_NS_URI, "MacAddr");
	private static final QName QNAME_WSU_TIMESTAMP = new QName(WSU_NS_URI, "Timestamp");
	private static final QName QNAME_WSU_CREATED = new QName(WSU_NS_URI, "Created");
	private static final QName QNAME_WSU_EXPIRES = new QName(WSU_NS_URI, "Expires");

	
	private String buildPasswordDigest(String userName, String password, String nonce, String dateTime) {
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

	
	@Override
	public boolean handleMessage(SOAPMessageContext context) {
//		System.out.println("Server : handleMessage()......");
		SOAPMessage soapMsg = context.getMessage();
		
		Boolean outbound  = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if ((outbound  != null) && (!outbound.booleanValue())) {
            handleInboundMessage(context);
            
            if (context.get("USERNAME") == null || context.get("PASSWORD") == null) {
            	generateSOAPErrMessage(soapMsg, "Access is denied.");
            	
     	       //tracking
     	       try {
				soapMsg.writeTo(System.out);
     	       } catch (SOAPException e) {
				// TODO Auto-generated catch block
     	    	   e.printStackTrace();
     	       } catch (IOException e) {
				// TODO Auto-generated catch block
     	    	   e.printStackTrace();
     	       }
           	
            	return false;
            	
            }
            
            if (typePassword.equals("PasswordText") && wsuId.equals("XWSSGID-1287429087296-1526798023")) {
	            if (context.get("USERNAME").equals("MyProject") &&
	            		context.get("PASSWORD").equals("welcome")) {
	            	
	//            	System.out.println("--------------------Mac Addr  : " + context.get("MACADDR"));
	            	return true;
	            } else {
	            	
					generateSOAPErrMessage(soapMsg,	"Invalid username, access is denied.");

					// tracking
					try {
						soapMsg.writeTo(System.out);
					} catch (SOAPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					return false;
	            }
	            
            } else {
            	DateTime dt = new DateTime();
            	DateTime dtwsseCreated = new DateTime(context.get("CREATED").toString());
            	DateTime dtwsuCreated = new DateTime(wsuCreated);
            	DateTime dtwsuExpires = new DateTime(wsuExpires);
            	
            	Duration duration = new Duration(dtwsuExpires, dt);
            	String passwordDigest = buildPasswordDigest(context.get("USERNAME").toString(), 
                                                            "welcome", 
                                                            context.get("NONCE").toString(),
                                                            context.get("CREATED").toString());

 /*           	
            	System.out.println("dt            --   " + dt);
            	System.out.println("dtwsseCreated --   " + dtwsseCreated);
            	System.out.println("dtwsuCreated  --   " + dtwsuCreated);
            	System.out.println("dtwsuExpires  --   " + dtwsuExpires);
            	System.out.println("wsuId         --   " + wsuId);
            	
            	System.out.println("username --------  " + context.get("USERNAME"));
            	System.out.println("password --------  " + context.get("PASSWORD"));
            	System.out.println("nonce    --------  " + context.get("NONCE"));
            	System.out.println("time     --------  " + context.get("CREATED"));
           	    System.out.println("digest   --------  " + passwordDigest);		                                          
            	System.out.println("dif time --------  " + duration.getStandardSeconds());
            	System.out.println("WSU:ID   --------  " + wsuId.equals("XWSSGID-1287429087296-1526798013"));
*/            	
            	if (wsuId.equals("XWSSGID-1287429087296-1526798013")) {
	            	if (duration.getStandardSeconds() < 0 &&
	            					passwordDigest.equals(context.get("PASSWORD").toString())) {
	            		
	            		return true;
	            	}
	            }
            	
				generateSOAPErrMessage(soapMsg,	"Invalid username, access is denied.");

				// tracking
				try {
					soapMsg.writeTo(System.out);
				} catch (SOAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return false;
            }
        }

	  return true;
	}

	/**
	 * Returns the message encoding (e.g. utf-8)
	 *
	 * @param msg
	 * @return
	 * @throws javax.xml.soap.SOAPException
	 */
	private String getMessageEncoding (SOAPMessage msg) throws SOAPException {
		String encoding = "utf-8";
    
		if (msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING) != null) {
			encoding = msg.getProperty(SOAPMessage.CHARACTER_SET_ENCODING).toString();
		}
    
		return encoding;
	}
	  
	  
	/**
	 * Dump SOAP Message to console
	 *
	 * @param msg
	 */
	private void dumpSOAPMessage (SOAPMessage msg) {
		
		if (msg == null) {
			System.out.println("SOAP Message is null");
			return;
		}
    
		System.out.println ("");
		System.out.println ("--------------------");
		System.out.println ("DUMP OF SOAP MESSAGE");
		System.out.println ("--------------------");
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			msg.writeTo(baos);
			System.out.println ("BAOS" + baos.toString(getMessageEncoding(msg)));
			
			// show included values
			String values = msg.getSOAPBody().getTextContent();
			System.out.println ("Included values: " + values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

  
	private void handleInboundMessage(SOAPMessageContext context) {
	    String wsseUsername = null;
	    String wssePassword = null;
	    String wsseNonce    = null;
	    String wsseCreated  = null;
	    String wsseMacAddr  = null;

        // INBOUND
        System.out.println ("Direction=inbound (handleMessage)");
        SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
        dumpSOAPMessage(msg);
       
	    try {
	        SOAPHeader header = context.getMessage().getSOAPHeader();
	        Iterator<?> headerElements = header.examineAllHeaderElements();
	        
	        while (headerElements.hasNext()) {
	            SOAPHeaderElement headerElement = (SOAPHeaderElement) headerElements.next();
	            
	            System.out.println("Header ----   : " + headerElement.getElementName().getLocalName());
	            
	            if (headerElement.getElementName().getLocalName().equals("Security")) {
	                SOAPHeaderElement securityElement = headerElement;
	                Iterator<?> it2 = securityElement.getChildElements();
	                
	                while (it2.hasNext()) {
	                    Node soapNode = (Node) it2.next();

 	                    if (soapNode instanceof SOAPElement) {
	                        SOAPElement element = (SOAPElement) soapNode;
	                        QName elementQname = element.getElementQName();
	                        
	                        System.out.println("Header ----   : " + elementQname);
	                        System.out.println("Id ----   : " + element.getAttribute("wsu:Id"));
	                        
	                        if (element.getAttribute("wsu:Id").length() > 0) {
	                        	wsuId = element.getAttribute("wsu:Id");
	                        }
	                        
	                        if (QNAME_WSU_TIMESTAMP.equals(elementQname)) {
	                            SOAPElement TimestampElement = element;
	                            
	                            wsuCreated = getFirstChildElementValue(TimestampElement, QNAME_WSU_CREATED);
	                            wsuExpires = getFirstChildElementValue(TimestampElement, QNAME_WSU_EXPIRES);
	                        	
	                        }
	                        
	                        if (QNAME_WSSE_USERNAMETOKEN.equals(elementQname)) {
	                            SOAPElement usernameTokenElement = element;
	                            
	                            wsseUsername = getFirstChildElementValue(usernameTokenElement, QNAME_WSSE_USERNAME);
	                            wssePassword = getFirstChildElementValue(usernameTokenElement, QNAME_WSSE_PASSWORD);
	                            wsseMacAddr = getFirstChildElementValue(usernameTokenElement, QNAME_WSSE_MACADDRS);
	                            wsseNonce = getFirstChildElementValue(usernameTokenElement, QNAME_WSSE_NONCE);
	                            wsseCreated = getFirstChildElementValue(usernameTokenElement, QNAME_WSSE_CREATED);
	                            
	                            break;
	                        }
	                    }
	
	                    if (wsseUsername != null) {
	                        break;
	                    }
	                }
	            }
	            
	            context.put("USERNAME", wsseUsername);
	            context.setScope("USERNAME", Scope.APPLICATION);
	
	            context.put("PASSWORD", wssePassword);
	            context.setScope("PASSWORD", Scope.APPLICATION);
	            
	            context.put("NONCE", wsseNonce);
	            context.setScope("NONCE", Scope.APPLICATION);

	            context.put("CREATED", wsseCreated);
	            context.setScope("CREATED", Scope.APPLICATION);

	            context.put("MACADDR", wsseMacAddr);
	            context.setScope("MACADDR", Scope.APPLICATION);

	        }
	    } catch (Exception e) {
	        System.out.println("Error reading SOAP message context: " + e);
	        e.printStackTrace();
	    }
	    
	    
	}
    
	
    private String getFirstChildElementValue(SOAPElement soapElement, QName qNameToFind) {
       String value = null;
       Iterator<?> it = soapElement.getChildElements(qNameToFind);
        
       while (it.hasNext()) {
           SOAPElement element = (SOAPElement) it.next(); //use first
           if (element.getElementQName().getLocalPart().contains("Password")) {
        	   if (element.getAttribute("Type").contains("PasswordDigest")) {
        		   typePassword = "PasswordDigest";
        	   } else {
        		   typePassword = "PasswordText";
        	   }
           }
           value = element.getValue();
        }
        
        return value;
    }
    
    
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void close(MessageContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<QName> getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	
    private void generateSOAPErrMessage(SOAPMessage msg, String reason) {
    
       try {
           SOAPBody soapBody = msg.getSOAPPart().getEnvelope().getBody();
           SOAPFault soapFault = soapBody.addFault();
           
           soapFault.setFaultString(reason);
           
           throw new SOAPFaultException(soapFault); 
        } catch(SOAPException e) { 
        	
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			System.out.println("generateSOAPErrMessage : " + errors.toString());
       	
        }
  
    }

}
