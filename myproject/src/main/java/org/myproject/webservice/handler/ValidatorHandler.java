package org.myproject.webservice.handler;


import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
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


public class ValidatorHandler implements SOAPHandler<SOAPMessageContext> {

	private static final Logger LOGGER = Logger.getLogger(ValidatorHandler.class);

	private static final String WSSE_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
	private static final QName QNAME_WSSE_USERNAMETOKEN = new QName(WSSE_NS_URI, "UsernameToken");
	private static final QName QNAME_WSSE_USERNAME = new QName(WSSE_NS_URI, "Username");
	private static final QName QNAME_WSSE_PASSWORD = new QName(WSSE_NS_URI, "Password");
	private static final QName QNAME_WSSE_MACADDRS = new QName(WSSE_NS_URI, "MacAddr");

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
//		System.out.println("Server : handleMessage()......");
		SOAPMessage soapMsg = context.getMessage();
		
		Boolean outbound  = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if ((outbound  != null) && (!outbound .booleanValue())) {
            handleInboundMessage(context);
            
            if (context.get("USERNAME").equals("MyProject") &&
            		context.get("PASSWORD").equals("welcome")) {
            	
//            	System.out.println("--------------------Mac Addr  : " + context.get("MACADDR"));
            	return true;
            } else {
            	
            	generateSOAPErrMessage(soapMsg, "Invalid username, access is denied.");
            	
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
        }

	  return true;
	}

	
	private void handleInboundMessage(SOAPMessageContext context) {
	    String wsseUsername = null;
	    String wssePassword = null;
	    String wsseMacAddr  = null;
	    
	    try {
	        SOAPHeader header = context.getMessage().getSOAPHeader();
	        Iterator<?> headerElements = header.examineAllHeaderElements();
	        
	        while (headerElements.hasNext()) {
	            SOAPHeaderElement headerElement = (SOAPHeaderElement) headerElements.next();
	            
	            if (headerElement.getElementName().getLocalName().equals("Security")) {
	                SOAPHeaderElement securityElement = headerElement;
	                Iterator<?> it2 = securityElement.getChildElements();
	                
	                while (it2.hasNext()) {
	                    Node soapNode = (Node) it2.next();

 	                    if (soapNode instanceof SOAPElement) {
	                        SOAPElement element = (SOAPElement) soapNode;
	                        QName elementQname = element.getElementQName();
	                        
	                        System.out.println("soapnode  : " + element.getValue());
	                        
	                        if (QNAME_WSSE_USERNAMETOKEN.equals(elementQname)) {
	                            SOAPElement usernameTokenElement = element;
	                            
	                            wsseUsername = getFirstChildElementValue(usernameTokenElement, QNAME_WSSE_USERNAME);
	                            wssePassword = getFirstChildElementValue(usernameTokenElement, QNAME_WSSE_PASSWORD);
	                            wsseMacAddr = getFirstChildElementValue(usernameTokenElement, QNAME_WSSE_MACADDRS);
	                            
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
            value = element.getValue();
        }
        
        return value;
    }
    
    
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
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
        	
        }
    }

}
