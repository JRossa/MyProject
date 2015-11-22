package org.myproject.model.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "tbl_SESSION")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class LogSession extends BaseEntity<Long> {

    
    private static final long serialVersionUID = 5502712619896419952L;

    static Logger logger = LoggerFactory.getLogger(LogSession.class);
 
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="USER_ID")
    private LogUser user;
    
    @Column(name = "ACTIVE")
    private Boolean active;
    
//  @Temporal(TemporalType.DATE)
//  @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "START_DATE")
    private Date startDate;    
    
//  @Temporal(TemporalType.DATE)
//  @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "SESSIONID")
    private String sessionId;
    
    
    public LogSession() {
    }

    public LogSession(LogUser user, Date startDate, Date endDate) {
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public LogUser getUser() {
        return user;
    }

    public void setUser(LogUser user) {
        this.user = user;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
  
}
