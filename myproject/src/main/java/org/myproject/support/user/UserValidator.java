package org.myproject.support.user;

import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;




import javax.inject.Named;

import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


/* 
 * 
 * The @FacesValidator isn't managed by the injection container. 
   Use Spring's @Component, CDI's @Named or JSF's @ManagedBean instead of @FacesValidator 
   in order to make it eligible for dependency injection.
   
   And reference it as

        <h:inputText ... validator="#{emailExistValidator.validate}" />
    or

        <f:validator binding="#{emailExistValidator}" />
    instead of

        <h:inputText ... validator="emailExistValidator" />
    or

        <f:validator validatorId="emailExistValidator" />
*/

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION)
//@FacesValidator("org.myproject.support.user.UserValidator")
public class UserValidator implements Validator {

    @Autowired
    private UserRepository userRepository;

     
    @Override
    public void validate(FacesContext context, UIComponent component,
                         Object value) throws ValidatorException {
        
        String username =  value.toString();
        LogUser user = null;
        
        if (username != null) {
            // System.out.println("UserValidation  :  " + username);
        
            user = this.userRepository.findByUserName(username);
        } else {
            FacesMessage msg = new FacesMessage("Username validation failed.", 
                    "Null username.");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }

        if(user != null) {
            // System.out.println("UserValidation Passou :  " + value.toString());
            
            FacesMessage msg = new FacesMessage("Username validation failed.", 
                                getResourceProperty("labels", "registration_usernameinvalid"));
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
        
    }

    public String getResourceProperty(String resource, String label) {

        Application application = FacesContext.getCurrentInstance().getApplication();
        ResourceBundle bundle = application.getResourceBundle(FacesContext.getCurrentInstance(), resource);

        return bundle.getString(label);
    }

}
