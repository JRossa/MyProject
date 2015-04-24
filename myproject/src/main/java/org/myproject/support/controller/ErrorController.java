package org.myproject.support.controller;

import java.security.Principal;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.myproject.model.entities.LogSession;
import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.LogSessionRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.support.user.UserMBean;

@Controller
@RequestMapping(value = { "" })
public class ErrorController {

    private static final Logger logger = Logger.getLogger(ErrorController.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private LogSessionRepository sessionRepository;

    
    @RequestMapping("/403")
    public ModelAndView accessDenied(Principal principal) {
        ModelAndView model = new ModelAndView();

        if (principal != null) {
            model.addObject("msg", "Hi " + principal.getName() + ", you do not have permission to access this page!");
        } else {
            model.addObject("msg", "You do not have permission to access this page!");
        }

        model.setViewName("login/403");
        return model;
    }

    
    @ResponseBody   
    @RequestMapping("/login")   //@ModelAttribute("time") => model.addObject("time", true);
    public ModelAndView loginTime(@RequestParam("time") String val) {
        ModelAndView model = new ModelAndView();
         
	    System.out.println("Val :" + val);
	    
		if (val != null) {	
			System.out.println("Logout Time Controller : " + val.toString());
	       	    
	       	if (val.toString().equals("true")) {
	       	   ;
	
	       	} else {
	       	   ;
	       	}
		        
		}
	    
        // To return to initial page public/main
		model.setViewName("public/public");
    	return model;
    
    }
    
    // Exemplo
    @RequestMapping(value = "/staticPage", method = RequestMethod.GET)
    public String redirect() {
      
       return "redirect:/pages/final.htm";
    }

}
