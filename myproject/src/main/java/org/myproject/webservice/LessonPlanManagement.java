package org.myproject.webservice;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;

import org.myproject.dao.LessonPlanUser;



import org.myproject.dao.LogUserDao;
import org.jboss.ws.api.annotation.EndpointConfig;


/*
 * https://docs.jboss.org/author/display/WFLY8/WS-Security
 * http://stackoverflow.com/questions/12319087/ws-security-endpointconfig-not-working?rq=1
 * http://svn.apache.org/viewvc/cxf/trunk/systests/ws-security-examples/src/test/resources/org/apache/cxf/systest/wssec/examples/ut/DoubleItUt.wsdl?view=markup
 * 
 *
@WebService
(
   portName = "SecurityServicePort",
   serviceName = "SecurityService",
   wsdlLocation = "WEB-INF/wsdl/SecurityService.wsdl",
   targetNamespace = "http://www.jboss.org/jbossws/ws-extensions/wssecuritypolicy",
   endpointInterface = "org.jboss.test.ws.jaxws.samples.wsse.policy.basic.ServiceIface"
)
@EndpointConfig(configFile = "WEB-INF/jaxws-endpoint-config.xml", configName = "Custom WS-Security Endpoint")

@EndpointProperties(value = {
	      @EndpointProperty(key = "ws-security.signature.properties", value = "bob.properties"),
	      @EndpointProperty(key = "ws-security.encryption.properties", value = "bob.properties"),
	      @EndpointProperty(key = "ws-security.signature.username", value = "bob"),
	      @EndpointProperty(key = "ws-security.encryption.username", value = "alice"),
	      @EndpointProperty(key = "ws-security.callback-handler", value = "org.jboss.test.ws.jaxws.samples.wsse.policy.basic.KeystorePasswordCallback")
	      }
	)

	// SOAP 1.2
		<dependency>
			<groupId>javax.xml.ws</groupId>
			<artifactId>jaxws-api</artifactId>
			<version>2.2.11</version>
		</dependency>
		@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
*/


@WebService(name="LessonPlanManagement",
		    serviceName="lessonplan/lessonplanws",
		    portName="LessonPlanManagementPort",
            targetNamespace = "http://www.jboss.org/jbossws/ws-extensions/wssecuritypolicy"
           )
@EndpointConfig(configFile = "WEB-INF/jaxws-endpoint-config.xml", configName = "Custom WS-Security Endpoint")
// http://stackoverflow.com/questions/5324051/webservices-bare-vs-wrapped
@SOAPBinding(style = SOAPBinding.Style.RPC, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@HandlerChain(file="./jaxws-handler-chain.xml")
public class LessonPlanManagement implements LessonPlanManagementWS {

	@Resource
	private LogUserDao user = new LogUserDao();
	
	final static String PATH = "D:\\mtomtest\\upload\\";
	
	@Resource
    private WebServiceContext ctx;
 
    public String getSessionId() {
    	MessageContext mc = ctx.getMessageContext();
    	HttpSession session = ((javax.servlet.http.HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST)).getSession();
 
    	if (session == null) {
    		throw new WebServiceException("No http Session Found");
    	}

    	
    	System.out.println("SessionId : " + session.getId());
    	return session.getId().toString();
   }
    

	@Override
	@WebMethod
	public String getAuthentication(String mobilePhone, String passwd) {

		String sessionId = this.getSessionId();
        
		return user.autenticateUser(mobilePhone, passwd, sessionId);
	}


	@Override
	@Oneway
	@WebMethod
	public void setFinished(String sessionId) {

		user.setUser(sessionId);
		
	}

	
	@Override
	@WebMethod
	public ArrayList<LessonPlanUser> getData(String sessionId) {
		
		return user.getLessonPlanData (sessionId);
	}


	@Override
	@WebMethod
	public Integer setData(String sessionId, String title, String lessonPlan, String lessonDate) {
		
		return user.setData(sessionId, title, lessonPlan, lessonDate);
		
	}

 
}
