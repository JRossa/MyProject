package org.myproject.support.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.LogUserRequest;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.repositories.UserRequestRepository;
import org.myproject.support.user.UserCRUDMBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = { "/request" })
public class UserRequestController {

	
    @Inject
    private UserRepository userRepository;

    @Inject
    private UserRequestRepository userRequestRepository;
	
    @RequestMapping("/activatepwd")  // rest/?num1=55&num2=45  @Path("/add/{a}/{b}")
    public ModelAndView activatePassword(@RequestParam("tag") String tag, 
    		                                HttpServletRequest request, HttpServletResponse response) {
    	ModelAndView model = new ModelAndView();
    	
	    if (tag.isEmpty()) {
			model.setViewName("public/public");
	    	return model;
	    }
	    
	    System.out.println("tag :" + tag);

	    LogUserRequest userRequest = this.userRequestRepository.findByUserRequestTag(tag);
	    
    	if (userRequest != null) {
    	    System.out.println("userRequest :" + userRequest.getUserId().getUserName());
    		if (userRequest.getEnabled().equals(true)) {
	    	    LogUser user = this.userRepository.findOne(userRequest.getUserId().getId());
	    		
	    		
	    		if (user != null) {
	    		   	System.out.println("Password ativated !!!  " + user.getUserName());
	    		   	Date today = new Date();
	    		   	Long diffTime = today.getTime() - userRequest.getExpiresDate().getTime();
	    		   	System.out.println("Dif days !!!  " + diffTime);
	   			    
	    		   	if (diffTime < 0 ) {
	    		     	user.setPassword(userRequest.getRequestData());
			        	user.setRndPassword(""); 
			        	user.setChangePassword(true);
			            user.setEnabled(true);
			            user.setAttempts(0);
			            user.setModificationUser("request - " + userRequest.getRequestTag());
			            user.setModificationDate(new Date());
			            userRepository.save(user);
	    		   	} 
			            
	     	        userRequest.setEnabled(false);
	     	        userRequest.setCreationDate(new Date());
	     	        userRequestRepository.save(userRequest);
	    		}
    		}
    		
    	}
	    
    	//bake cookie
	    Cookie myCookie = null;
	    
		try {
			myCookie = new Cookie("MyProject", URLEncoder.encode(tag, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			myCookie = new Cookie("MyProject", tag);
		} 
	    
	    //set expire time to 1000 sec
	    myCookie.setMaxAge(1000); 
	             
	    //put cookie in response
	    response.addCookie(myCookie); //put cookie in response 

	    Cookie[] myCookies = null;
	    
	    // Get an array of Cookies associated with this domain
	    myCookies = request.getCookies();
	    
	    if( myCookies != null ){
	       System.out.println("Found Cookies Name and Value");
	       
	       for (Cookie c: myCookies){
	          
	          if((c.getName()).compareTo("first_name") == 0 ) {
	        	  c.setMaxAge(0);
	              response.addCookie(c);
	              System.out.println("Deleted cookie: " + myCookie.getName());
	          }
	          
	          System.out.println("Name : " + c.getName());
	          System.out.println("Value: " + c.getValue());
	       }
	    }
	    
        // To return to initial page public/main
		model.setViewName("public/public");
    	return model;
  
    }
}
