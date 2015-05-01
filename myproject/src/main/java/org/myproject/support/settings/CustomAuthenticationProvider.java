package org.myproject.support.settings;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.EncryptHash;
import org.myproject.model.utils.PasswordHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

@Named
public class CustomAuthenticationProvider implements AuthenticationProvider {

    static Logger          logger = LoggerFactory.getLogger(LogUser.class);

    @Inject
    private UserRepository userRepository;

    public CustomAuthenticationProvider() {
        super();
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        LogUser user = null;
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Boolean test = true;
        
        logger.info("User (Authentication) : " + username);
        
        if (username.equals("admin")) {
            user = this.userRepository.findByUserNameAndRndPassword(username, password);

//            logger.info("User  : " + user);
            
            if (user != null && !test) {
                if (!userAttemptsManager (user)) {
                    return null;
                }    
            }
        }
        
        
        if (user == null) {
            logger.info("User  2nd attempt : " + username);

            // New - 2nd change to Admin
            user = this.userRepository.findByUserName(username);

            if (!userAttemptsManager (user)) {
                return null;
            }
            
            try {
                String passwordStored = user.getPassword();
                logger.info("Pass & Encrypt :  " + password + "  &  " + passwordStored);
            
                if (!PasswordHash.validatePassword(password, passwordStored)) {
                    return null;
                }  
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }
        
        
        if (user != null) {
        	
        	if (user.getRndPassword() != null && user.getRndPassword().length() > 0) {
        		// TODO - no final não permitir entrar
        		logger.info("Password em claro : ");
        		return null;
        	}
        	
            logger.info("auth role user not null : ");
            
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

            logger.info("auth role user : " + user.getLogRole().getRolename());

            grantedAuthorities.add(new SimpleGrantedAuthority(user.getLogRole().getRolename()));

            UserDetails userDetails = new User(username, password, grantedAuthorities);

            return new UsernamePasswordAuthenticationToken(userDetails, password, grantedAuthorities);
        }
        
        return null;
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    
    public Boolean userAttemptsManager (LogUser user) {
        int maxAttempts = 3;

        if (user == null) {
            return false;
        }

        if (!user.getEnabled()) {
            return false;
        }
        
        if (user.getAttempts() == maxAttempts) {
            user.setEnabled(false);
            this.userRepository.saveAndFlush(user);
            return false;
        } else {
            user.setAttempts(user.getAttempts() + 1);
            this.userRepository.saveAndFlush(user);
        }
        
        return true;
    }



}

/*
 * http://www.programcreek.com/java-api-examples/index.php?api=org.springframework
 * .security.authentication.UsernamePasswordAuthenticationToken
 * 
 * Code Example 14:
 * 
 * 5
 * 
 * vote From project ananya-batch, under directory
 * /src/main/java/org/motechproject/ananyabatch/. Source
 * AuthenticationProvider.java
 * 
 * @Override protected UserDetails retrieveUser(String
 * username,UsernamePasswordAuthenticationToken authentication) throws
 * AuthenticationException { String
 * password=(String)authentication.getCredentials(); try { if
 * ("SKNltM4eMipVrpAX89rwwA==".equals(encrypt(username)) &&
 * "DzWXQL0c2plPi1UzDIbYRQ==".equals(encrypt(password))) {
 * Collection<GrantedAuthority> a=new ArrayList<GrantedAuthority>(); a.add(new
 * GrantedAuthorityImpl("admin")); return new
 * User("admin","pass",true,true,true,true,a); } } catch ( Exception ignored) {
 * } throw new BadCredentialsException("Invalid password"); }
 * 
 * 
 * 
 * From project brix-cms-plugins, under directory
 * /examples/example-springsecurity/src/main/java/brix/demo/web/auth/.
 * 
 * Source LoginPage.java
 * 
 * 
 * 
 * public LoginPage(){ add(new FeedbackPanel("feedback")); setDefaultModel(new
 * CompoundPropertyModel(this)); Form form=new Form("login"){
 * 
 * @Override protected void onSubmit(){ super.onSubmit(); Authentication
 * authResult=null; try { authResult=authenticationManager.authenticate(new
 * UsernamePasswordAuthenticationToken(username,password)); } catch ( Exception
 * e) { log.debug("failed login",e); } if (authResult != null) {
 * setAuthentication(authResult); if
 * (!getPage().continueToOriginalDestination()) {
 * setResponsePage(getApplication().getHomePage()); } } else {
 * error(getLocalizer().getString("exception.login",this)); } } } ; form.add(new
 * TextField("username")); form.add(new PasswordTextField("password"));
 * form.add(new Button("submit")); add(form); }
 */
