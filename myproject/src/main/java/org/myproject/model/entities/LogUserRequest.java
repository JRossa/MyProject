package org.myproject.model.entities;

import org.myproject.model.utils.BaseEntity;
import org.myproject.model.utils.EncryptHash;

import java.security.MessageDigest;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tbl_USER_REQUEST")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class LogUserRequest extends BaseEntity<Long> {

	private static final long serialVersionUID = -775877657260709240L;

	static Logger logger = LoggerFactory.getLogger(LogUserRequest.class);

    // FetchType.LAZY => Erro no hibernate - could not initialize proxy - no
    // Session
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private LogUser userId;

    @Column(name = "REQUEST_NAME")
    private String requestName;

    @Column(name = "REQUEST_TAG", unique = true)
    private String requestTag;

    @Column(name = "REQUEST_DATA")
    private String requestData;

    @Column(name = "CHANGE_PASSWORD")
    private Boolean changePassword;

    @Column(name = "ENABLED")
    private Boolean enabled;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "EXPIRES_DATE")
    private Date expiresDate;

    
	public LogUser getUserId() {
		return userId;
	}

	
	public void setUserId(LogUser userId) {
		this.userId = userId;
	}

	
	public String getRequestName() {
		return requestName;
	}

	
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	
	public String getRequestTag() {
		return requestTag;
	}

	
	public void setRequestTag(String requestTag) {
		this.requestTag = requestTag;
	}

	
	public String getRequestData() {
		return requestData;
	}

	
	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	
	public Boolean getChangePassword() {
		return changePassword;
	}

	
	public void setChangePassword(Boolean changePassword) {
		this.changePassword = changePassword;
	}

	
	public Boolean getEnabled() {
		return enabled;
	}

	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	
	public Date getCreationDate() {
		return creationDate;
	}

	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	
	public Date getExpiresDate() {
		return expiresDate;
	}

	
	public void setExpiresDate(Date expiresDate) {
		this.expiresDate = expiresDate;
	}

}
