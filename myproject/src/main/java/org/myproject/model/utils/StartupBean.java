package org.myproject.model.utils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;


import org.myproject.support.clock.ClockMBean;


@Singleton
@Startup
public class StartupBean {
    public enum States {BEFORESTARTED, STARTED, PAUSED, SHUTTINGDOWN};
    private States state;
    
        
    @PostConstruct
    public void initialize() {
        state = States.BEFORESTARTED;
        // Perform intialization
        state = States.STARTED;
        
//        ClockMBean clock = new ClockMBean();
//        clock.setupAlarm();
        
        System.out.println("Service Started");
    }
  
    
    @PreDestroy
    public void terminate() {
        state = States.SHUTTINGDOWN;
        // Perform termination
        System.out.println("Shut down in progress");
    }
    
    public States getState() {
        return state;
    }
    
    public void setState(States state) {
        this.state = state;
    }
}