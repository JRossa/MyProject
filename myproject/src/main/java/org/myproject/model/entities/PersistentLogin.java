package org.myproject.model.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.myproject.model.utils.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Entity
@Table(name = "tbl_PERSISTENT_LOGINS")
public class  PersistentLogin  {


    static Logger logger = LoggerFactory.getLogger(PersistentLogin.class);
    
    @Column(name = "USERNAME")
    private String username;
    
    @Id
    @Column(name = "SERIES")
    private String series;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "LAST_USED")
    private Date last_used;

    
    
    
	public PersistentLogin() {
		super();
	}
	

	public PersistentLogin(String username, String series, String token) {
		super();
		this.username = username;
		this.series = series;
		this.token = token;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLast_used() {
		return last_used;
	}

	public void setLast_used(Date last_used) {
		this.last_used = last_used;
	}
    
    
}
