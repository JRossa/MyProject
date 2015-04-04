package org.myproject.support.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.PasswordHash;
import org.primefaces.validate.ClientValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
public class PasswordValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMBean userMBean;

    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        
        String username =  userMBean.getUserName();
        String password = value.toString();
        LogUser user = null;
        
        if (username != null && password != null) {
            System.out.println("PassordValidator  :  " + username + "   " + password);
        
            user = this.userRepository.findByUserName(username);
        } else {
            FacesMessage msg = new FacesMessage("Password validation failed.", "Null password.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

        if(user == null) {
             System.out.println("PassordValidator Passou :  " + value.toString());
            
            FacesMessage msg = new FacesMessage("Password validation failed.", "Invalid username.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            
            throw new ValidatorException(msg);
        } else {
            String passwordStored = user.getPassword();
            
            try {
                System.out.println("PassordValidator  compare :  " + password + "   " + passwordStored);
                if (!PasswordHash.validatePassword(password, passwordStored)) {
                    
                    FacesMessage msg = new FacesMessage("Password validation failed.", "Invalid username.");
                    
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                 }
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
        }
            
        
    }


}
