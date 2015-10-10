package org.myproject.support.logsession;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.myproject.model.entities.LogSession;
import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.LogSessionRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.support.user.UserMBean;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "sessionMBean")
public class SessionMBean extends BaseBean {

    private static final long serialVersionUID = -9222669759092086306L;

    private static final Logger logger = Logger.getLogger(SessionMBean.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private LogSessionRepository sessionRepository;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    ATTENTION: cannot inject a managed bean  - because it is referenced in ctx-myproject.xml to be called by 
//               SessionExpiredListener -> sessionEnd (implements HttpSessionListener)  
//    SOLUTION:  create a separate LogSessionCRUD.java to do CRUD actions
//    
//    @Inject
//    private LogSessionMBean mbLogSession;
//
//    @Inject
//    private FacesContext context;
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    

    private boolean pollStop = false;
    
    private boolean pollRender = true;
    
    private int pollCount = 0;


    public SessionMBean() {
        
    }
  
    
    public void sessionCreated()  {
        
        System.out.println("Login Btn");
        
    }
    
    public void sessionStart(String username) {
      LogUser user = this.userRepository.findByUserName(username);
      LogSession session = new LogSession();
      
      session.setUser(user);
      session.setActive(true);
      session.setStartDate(new Date());
      session.setEndDate(new Date());
      
      this.sessionRepository.saveAndFlush(session);
    }
 
    public void sessionEnd(String username) {
 
    	if (username == null) {
    		return;
    	}
    	
    	System.out.println("endSession username : " + username);

    	LogUser user = this.userRepository.findByUserName(username);
    	Long sessionId = this.sessionRepository.findIdByUserAndIdActive(user.getId());

    	LogSession session = this.sessionRepository.findOne(sessionId);

    	session.setActive(false);  
    	session.setEndDate(new Date());
      
    	 this.sessionRepository.saveAndFlush(session);
    }
    
    public void sessionEnd(String username, Date startDate) {
    	
        System.out.println("endSession username : " + username);
        System.out.println("endSession startDate : " + startDate);
        
        LogUser user = this.userRepository.findByUserName(username);
        
        if (user != null) {
            Long sessionId = this.sessionRepository.findIdByUserAndStartDateAndIdActive(user.getId(), startDate);
            
            
            System.out.println("endSession userId : " + user.getId());
            System.out.println("endSession sessionId : " + sessionId);
            
            if (sessionId != null) {
                LogSession session = this.sessionRepository.findOne(sessionId);
    
                session.setActive(false);  
                session.setEndDate(new Date());
            
                System.out.println("endSession fim : " + sessionId);
                this.sessionRepository.saveAndFlush(session);
                
            }
        }
        
    }
    
    
    public void pollListener () {

        System.out.println("Poll Listener  : " + this.pollCount);
        
        if (this.pollCount == 5) {
            this.pollStop = true;
            this.pollRender = false;
        } else {
            this.pollCount++;
        }    
    }
    
    
    public boolean isPollRender() {
        return pollRender;
    }

    public void setPollRender(boolean pollRender) {
        this.pollRender = pollRender;
    }


    public boolean isPollStop() {
        return pollStop;
    }


    public void setPollStop(boolean pollStop) {
        this.pollStop = pollStop;
    }


    public void gotoURL(String url) {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
        
        try {
            System.out.println("Real Path  : " + context.getRequestContextPath());
            context.redirect(context.getRequestContextPath() + url);
             
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    private void setCookie(String cookieName, int expire) {
    	// String cookieName, String value, int expire
    	FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext(); 

        HttpServletRequest request = (HttpServletRequest) extContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) extContext.getResponse();

        Cookie cookie = null;
        
        Cookie[] userCookies = request.getCookies();
        
        if (userCookies != null && userCookies.length > 0) {
        	for (Cookie c: userCookies) {
        		
        		if (c.getName().equals(cookieName)) {
        			cookie = c;
        			break;
        		}
        	}
        }
        
        if (cookie != null) {
//        	cookie.setValue(value);
        } else {
//        	cookie =  new Cookie(cookieName, value);
            cookie =  new Cookie(cookieName, null);
        }
        
        cookie.setMaxAge(expire);
        cookie.setPath(StringUtils.hasLength(request.getContextPath()) ? request.getContextPath() : "/");

        response.addCookie(cookie);
    }
    
    
    public void sessionDestroyed()  {
    	FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext(); 
        
        System.out.println("Logout Btn");  
      
        String username = (String) extContext.getSessionMap().get("username");
        System.out.println("User name  : " + username);
      
        this.sessionEnd(username);
              
        String cookieName = "MY_PROJECT_COOKIE";
        // delete Cookie
        setCookie(cookieName, 0);

        this.gotoURL("/public/logout");
    }

    
    public void sessionExit()  {
     
        this.gotoURL("/public/logout");
    }
    

    
    public Boolean sessionActive() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    	FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext(); 

        String username = (String) extContext.getSessionMap().get("username");
        
//        System.out.println("sessionActive - user : " + username);  
       
        
        if (username == null) {
        	
        	// RememberMe 
        	
        	LogUser user = userRepository.findByUserName(authentication.getName());
/*        	
        	System.out.println("sessionActive  -  " + authentication.isAuthenticated());
        	System.out.println("sessionActive  -  " + authentication.getName());
        	System.out.println("sessionActive  -  " + authentication.getClass());
        	System.out.println("sessionActive  -  " + authentication.getPrincipal());
        	System.out.println("sessionActive  -  " + authentication);  
*/        	
        	if (user != null) {
//        		System.out.println("sessionActive  - user - " + user.getTeacher().getFullName());  
	            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
	            
	            username = user.getUserName();
	            
	            session.setAttribute("username", username);
	            session.setAttribute("rolename", user.getLogRole().getRolename());
	            session.setAttribute("userId", user.getId());
	            session.setAttribute("changepass", 0);

	            String logNameMessage = user.getTeacher().getFullName();
	            String lastLogMessage = user.getLastLoginDate().toString();
	
	            session.setAttribute("logNameMessage", logNameMessage);
	            
	            if (lastLogMessage != null)
	            	session.setAttribute("lastLogMessage", lastLogMessage);
        	}       

        }

        return (username != null);
    }
    
}
