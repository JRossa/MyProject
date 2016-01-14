package org.myproject.support.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = { "" })
public class OAuth2Controller {

    private static final Logger logger = Logger.getLogger(OAuth2Controller.class);

    
    @RequestMapping("/oauth2callback")
    public void oauth2Code(HttpServletRequest request, HttpServletResponse response) {
    	 
    	System.out.println("Code  :  " + request.getParameter("code"));
    	System.out.println("State  :  " + request.getParameter("state"));
    	System.out.println("Code  :  " + request.getParameter("access_token"));

    }   

    
    // Exemplo
    @RequestMapping(value = "/oauth2staticPage", method = RequestMethod.GET)
    public String redirectOAuth2() {
      
       return "redirect:/pages/final.htm";
    }
 
}
