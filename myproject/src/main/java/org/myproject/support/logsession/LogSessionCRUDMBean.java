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

@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@Named(value = "logSessionCRUDMBean")
public class LogSessionCRUDMBean extends BaseBean {

    private static final long serialVersionUID = -9222669759092086306L;

    private static final Logger logger = Logger.getLogger(LogSessionCRUDMBean.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private LogSessionRepository sessionRepository;

    @Inject
    private LogSessionMBean mbLogSessionBean;

    @Inject
    private FacesContext context;


    public LogSessionCRUDMBean() {
        
    }
 
    public void delete () {
        
        this.mbLogSessionBean.delete();
    }
    
    public void deleteZombiesSessions () {
        
        List<LogSession> sessions = this.sessionRepository.findAllActiveSessions();
        
        for (LogSession s: sessions) {
            this.sessionRepository.deleteAllUserZombiesSessions(s.getUser().getId(), s.getId());
        }
    }
     
}
