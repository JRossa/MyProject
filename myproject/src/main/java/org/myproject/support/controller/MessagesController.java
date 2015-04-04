package org.myproject.support.controller;

import javax.faces.event.ActionEvent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


public class MessagesController {

      
       
       public MessagesController () {
           FacesContext context= FacesContext.getCurrentInstance();
           FacesMessage msg = null;
           
           msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Wellcame", "Ivalid");
           msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Sample info message", "PrimeFaces rocks!");
           
           context.addMessage(null, msg);
           
       }
}
                        