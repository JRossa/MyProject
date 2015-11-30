package org.myproject.support.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//RESTful Servlet Test
// @Controller -> @ResponseBody <=> @RestController
@RestController
@RequestMapping(value = { "/rest" })
public class RESTfulController {

	
	 @RequestMapping(value = "/{name}", method = RequestMethod.GET)
	 public @ResponseBody String getGreeting(@PathVariable String name) {
		 String result="Hello "+name; 
	
		 return result;
	
	 }
	

    
    @ResponseBody   
    @RequestMapping("/add")  // rest/?num1=55&num2=45  @Path("/add/{a}/{b}")
    public String addTwo(@RequestParam("num1") int num1, @RequestParam("num2") int num2) {
         
	    System.out.println("num1 :" + num1 + "   num2 :" + num2);
	    
   	return num1 + num2 + "";
    
    }
	
}
