package org.myproject.webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;

import org.myproject.dao.LessonPlanUser;



import org.myproject.dao.LogUserDao;
import org.jboss.ws.api.annotation.EndpointConfig;


/*
 * https://docs.jboss.org/author/display/WFLY8/WS-Security
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

*/


@WebService(serviceName="lessonplan/lessonplanws",
            targetNamespace = "http://www.jboss.org/jbossws/ws-extensions/wssecuritypolicy")
@EndpointConfig(configFile = "WEB-INF/jaxws-endpoint-config.xml", configName = "Custom WS-Security Endpoint")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class LessonPlanManagement implements LessonPlanManagementWS {

	@Resource
	private LogUserDao user = new LogUserDao();
	
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
		System.out.println("SessionId : " + sessionId);
        
		return user.autenticateUser(mobilePhone, passwd, sessionId);
	}


	@Override
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
	public Integer setData(String sessionId, String title, String lessonPlan) {
		
		return user.setData(sessionId, title, lessonPlan);
		
	}
	
}
