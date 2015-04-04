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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    
    public void sessionDestroyed()  {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
        
        System.out.println("Logout Btn");  
      
        String username = (String) context.getSessionMap().get("username");
        System.out.println("User name  : " + username);
      
        this.sessionEnd(username);
      
        this.gotoURL("/public/logout");
    }
    
    
    public Boolean sessionActive() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
        
        String username = (String) context.getSessionMap().get("username");
        
        return (username != null);
    }
    
}
