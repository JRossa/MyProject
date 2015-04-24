package org.myproject.support.user;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
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
import org.myproject.model.utils.PasswordHash;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION)
@Named(value = "userMBean")
public class UserMBean extends BaseBean {

 
    private static final long serialVersionUID = -3478864392431503130L;

    private static final Logger logger = Logger.getLogger(UserMBean.class);
    
    @Inject
    private UserRepository userRepository;

    @Inject
    private LogSessionRepository sessionRepository;
 
    private List<LogUser> users;
    
    private LogUser selectedUser;
    
    private Long id;
    
    private String firstName;
    
    private String lastName;
    
    private String userName;
    
    private String password;
    
    private String newpassword;
    
    private String email;

    
    // Control Buttons
    private Boolean checkPassword;

    private Boolean changePassword;
    
    private Boolean lockResponse;

    private Boolean disableButtons;

    
    public UserMBean() {
        super();
        
        this.checkPassword = true;
        this.changePassword = true;
        this.lockResponse = false;
        this.disableButtons = false;
    }

    
 //   @PostConstruct    
    public void onLoad () {
        this.checkPassword = false;
        this.disableButtons = true;
        
        this.users = userRepository.findAll();
    }
    
//    @PostConstruct
    public void onLoadSelectedUser () {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
        String username = (String) context.getSessionMap().get("username");
        
        // System.out.println("check Log User  :  " + username);
        
        LogUser user = this.userRepository.findByUserName(username);
        
        if (user != null) {
            this.selectedUser = user;
            
            this.firstName = user.getFirstName();
            this.lastName  = user.getLastName();
            this.userName  = user.getUserName();
            
            
            System.out.println("check Log User (onLoadSelectedUser) :  " + this.firstName);
            System.out.println("check Log User (onLoadSelectedUser) :  " + this.lastName);
            System.out.println("check Log User (onLoadSelectedUser) :  " + this.userName);
            
            this.password = null;
            this.newpassword = null;
            
            //this.setPassword("admin");
            //this.setNewpassword("123456568");
        }
       
    }
    
    public List<LogUser> getUsers() {
        return users;
    }

    public void setUsers(List<LogUser> users) {
        this.users = users;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    
    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public void delete() {
        System.out.println("Delete  : ");

        System.out.println("Delete  : " + this.selectedUser.getUserName());
        
        if (this.selectedUser.getUserName().equals("admin")) {
            return;
        }
      
        if (this.selectedUser != null) {
//            System.out.println("Delete  : " + this.selectedUser.getId());
            
            List <LogSession> sessions = this.sessionRepository.findIdByUser(this.selectedUser.getId());
            
            for (LogSession s: sessions) {
                this.sessionRepository.delete(s.getId());
            }
            
            this.userRepository.delete(this.selectedUser.getId());
        }
    }


    public void selectUser(SelectEvent ev) {
        
        try {
            if (ev.getObject() != null) {
                this.selectedUser = (LogUser) ev.getObject();
                System.out.println("Passou");
            } else {
                this.selectedUser = null;
            }
        } catch (Exception e) {
            this.selectedUser = null;

            logger.error(e.getMessage(), e);
        }
    }

    public void unselectUser() {
        this.selectedUser = null;
    }

     public LogUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(LogUser selectedUser) {
        this.selectedUser = selectedUser;
    }

    
    public Boolean getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(Boolean checkPassword) {
        this.checkPassword = checkPassword;
    }


    public Boolean checkUserPassword ()  {
        // System.out.println("checkUserPassword  :  " + this.checkPassword);
    	
    	if (this.lockResponse == true) {
    		return this.checkPassword;
    	}
        FacesMessage message;
    	String msg = getResourceProperty("labels", "user_register_inserted") + "  ";
        String msgTitle = "AVISO :"  + " checkUserPassword";
        String msgBody  = "Deve mudar a password para que a sua conta não fique bloqueada !!!";
       
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext(); 
        String username = (String) context.getSessionMap().get("username");
        
        LogUser user = this.userRepository.findByUserName(username);

        if (user != null) {
        	// TODO - Validar se muda a password
            System.out.println("Check Log User  :  " + username + "  change : " + user.getChangePassword());

        	if (user.getRndPassword() == null || user.getRndPassword().length() == 0) {
                // Do nothing
        		if (user.getChangePassword().equals(false)) {
	                this.checkPassword = false;
	                this.lockResponse = true;
	                return false;
        		} else {
        			this.checkPassword = true;
        			this.lockResponse = true;
        			return true;
        		}
        			
                
        	} else {
                if (this.lockResponse == false) {
                	message = new FacesMessage(FacesMessage.SEVERITY_INFO, msgTitle, msgBody);
                	FacesContext.getCurrentInstance().addMessage(null, message);
                	
                	this.lockResponse = true;
                }
                
        		this.checkPassword = true;
        		return true;
        	}
        } 
        
     this.checkPassword = false;
     return false;
    }
 
    
    public void changePassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        if ( this.newpassword == null) {
            System.out.println("changePassword  :  " + this.newpassword);
        	return;
        }
        
        String msgTitle = getResourceProperty("labels", "user_warning") + " changePassord";
        String msgBody  = getResourceProperty("labels", "user_password_changed");
        
        
        if (this.selectedUser != null) {
        	this.selectedUser.setRndPassword("");
        	this.selectedUser.setPassword(PasswordHash.createHash(this.newpassword));
        	this.selectedUser.setChangePassword(false);
        	
        	this.userRepository.saveAndFlush(this.selectedUser);
        	
        	msgBody = msgBody + "   '" +  this.newpassword + "'";
        	
        	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msgTitle, msgBody);
            FacesContext.getCurrentInstance().addMessage(null, message);
            
            this.changePassword = false;
            this.checkPassword  = false;
        }
        
    }

    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Boolean getDisableButtons() {
        return disableButtons;
    }


    public void setDisableButtons(Boolean disableButtons) {
        this.disableButtons = disableButtons;
    }

    
    public void openChangePass () {
    	
        if (this.changePassword == false) {
            return;
        }
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext(); 
        String username = (String) externalContext.getSessionMap().get("username");

    	this.selectedUser = this.userRepository.findByUserName(username);
    	
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('changeInitPassDialog').show()");
    }
    
    
    public void openForgotPass () {
    	
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("PF('forgotPassDialog').show()");
    }
    
    
    public String getResourceProperty(String resource, String label) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }
    
}
