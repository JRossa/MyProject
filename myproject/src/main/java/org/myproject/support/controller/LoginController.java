package org.myproject.support.controller;

import java.security.Principal;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.myproject.model.entities.LogSession;
import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.LogSessionRepository;
import org.myproject.model.repositories.UserRepository;

@Controller
@RequestMapping(value = { "/public" })
public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private LogSessionRepository sessionRepository;

    @RequestMapping(value = "/visit")
    public ModelAndView visit() {
        String message = "visit page !";

        logger.warn("login Controller !!" + message);

        return new ModelAndView("public/public", "message", message);
    }

    @RequestMapping(value = "/main")
    public ModelAndView main() {
        String message = "visit page !";

        logger.warn("login Controller !!" + message);

        return new ModelAndView("public/public", "message", message);
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        String message = "visit page !";
        logger.warn("logout Controller !!" + message);
        
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // concern you
        
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
            
         }       
        

        return new ModelAndView("public/public", "message", message);
    }

    @RequestMapping("/login")
    public ModelAndView login(Principal principal) {
        ModelAndView model = new ModelAndView();

        if (principal != null) {
        	String username = principal.getName();
	        String logMessage = "Welcome :  " + username;
	        
	        System.out.println("Login Controller User:" + logMessage);
	
	        String rolename = getRole(username);
	
	        logger.debug("Directing to home page for: [" + rolename + "]");
	
	        if (rolename.equals("ROLE_ADMIN")) {
	            model.setViewName("pages/admin/admin");
	            model.addObject("logMessage", logMessage);
	        } else if (rolename.equals("ROLE_USER_T")) {
	            return new ModelAndView("public/main", "logMessage", logMessage);
	        } else if (rolename.equals("ROLE_TEACHER")) {
	            return new ModelAndView("pages/main", "logMessage", logMessage);
	        } else if (rolename.equals("ROLE_STUDENT")) {
	            return new ModelAndView("pages/main", "logMessage", logMessage);
	        } else { // NO_ROLE
	            return new ModelAndView("pages/public", "logMessage", logMessage);
	        }
        } else {
        	model.setViewName("public/public");
        }
        
        return model;
    }

    private String getRole(String username) {
        String rolename = "NO_ROLE";

        LogUser user = this.userRepository.findByUserName(username);

        if (user != null) {
            logger.debug("Roles " + user.getLogRole().getRolename());
            rolename = user.getLogRole().getRolename();
        } else {
            logger.error("User not found !!!");
        }

        return rolename;
    }

}

/*
 * @RequestMapping("/login") public ModelAndView login(Principal principal) {
 * ModelAndView model = new ModelAndView(); String username =
 * principal.getName(); String message = "Welcome :  " + username;
 * 
 * System.out.println("Login Controller User:" + message);
 * 
 * String rolename = getRole(username);
 * 
 * logger.debug("Directing to home page for: [" + rolename + "]");
 * 
 * if (rolename.equals("ROLE_ADMIN")) { return new ModelAndView("pages/main",
 * "message", message); } else if (rolename.equals("ROLE_USER_T")) { return new
 * ModelAndView("public/main", "message", message); } else if
 * (rolename.equals("ROLE_TEACHER")) { return new ModelAndView("pages/main",
 * "message", message); } else if (rolename.equals("ROLE_STUDENT")) { return new
 * ModelAndView("pages/main", "message", message); } else { // NO_ROLE return
 * new ModelAndView("pages/public", "message", message); }
 * 
 * return model }
 */