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
@Table(name = "tbl_USER")
@AttributeOverride(name = "id", column = @Column(name = "ID"))
public class LogUser extends BaseEntity<Long> {

    private static final long serialVersionUID = 201404140102L;

    static Logger logger = LoggerFactory.getLogger(LogUser.class);

    // FetchType.LAZY => Erro no hibernate - could not initialize proxy - no
    // Session
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID")
    private LogRole logRole;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;

    @Column(name = "USER_ID")
    private int userId;

    @Column(name = "STUDENT_ID")
    private int studentId;

    @Column(name = "USER", unique = true)
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "RND_PASSWORD")
    private String rndPassword;

    @Column(name = "CHANGE_PASSWORD")
    private Boolean changePassword;

    @Column(name = "ENABLED")
    private Boolean enabled;

    @Column(name = "CREATION_USER")
    private String creationUser;

//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "MODIFICATION_USER")
    private String modificationUser;

//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "MODIFICATION_DATE")
    private Date modificationDate;

//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "LASTLOGIN_DATE")
    private Date lastLoginDate;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "ATTEMPTS")
    private int attempts;
    
    
    
    public LogUser() {
        this.logRole = new LogRole();
        this.teacher = new Teacher();
        this.enabled = true;
    }

    public LogUser(String userName, String password) {
        super();

        this.userName = userName;
        this.password = password;
    }

    public LogRole getLogRole() {
        return logRole;
    }

    public void setLogRole(LogRole logRole) {
        this.logRole = logRole;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
       this.password = password;
    }

    public String getRndPassword() {
        return rndPassword;
    }

    public void setRndPassword(String rndPassword) {
        this.rndPassword = rndPassword;
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

    
    public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getModificationUser() {
        return modificationUser;
    }

    public void setModificationUser(String modificationUser) {
        this.modificationUser = modificationUser;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
    
}
