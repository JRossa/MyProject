package org.myproject.support.logsession;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.myproject.model.entities.LogSession;
import org.myproject.model.repositories.LogSessionRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.BaseBean;
import org.myproject.support.user.UserMBean;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "logSessionMBean")
public class LogSessionMBean extends BaseBean {

    private static final long serialVersionUID = 436504834785019782L;

    private static final Logger logger = Logger.getLogger(LogSessionMBean.class);
    
    @Inject
    private LogSessionRepository sessionRepository;

    private List<LogSession> sessions;
    
    private LogSession selectedSession;
    
    private Long id;

    

    public LogSessionMBean() {
        super();
    }
    
    
    public void onLoad () {
       
        this.sessions = this.sessionRepository.findAll();
        
//        for ( LogSession s: sessions) {
//            System.out.println("#          : " + s.getId());
//            System.out.println("Estado     : " + s.getActive());
//            System.out.println("Utilizador : " + s.getUser().getTeacher().getFullName());
//            System.out.println("Início     : " + s.getStartDate());
//            System.out.println("Fim        : " + s.getEndDate());
//            
//        }
    }

    public void onLoadActiveSessions () {
        
        this.sessions = this.sessionRepository.findAllActiveSessions();
    }
    
    
    public List<LogSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<LogSession> sessions) {
        this.sessions = sessions;
    }

    public LogSession getSelectedSession() {
        return selectedSession;
    }

    public void setSelectedSession(LogSession selectedSession) {
        this.selectedSession = selectedSession;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void delete() {
        System.out.println("Delete  : ");
        
        if (this.selectedSession != null) {
            this.sessionRepository.delete(this.selectedSession.getId());
        }
    }

    public void selectSession(SelectEvent ev) {
            
        try {
            if (ev.getObject() != null) {
                this.selectedSession = (LogSession) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectedSession = null;
            }
        } catch (Exception e) {
            this.selectedSession = null;

            logger.error(e.getMessage(), e);
        }
    }

    
    public void unselectSession() {
        this.selectedSession = null;
    }
        

}
