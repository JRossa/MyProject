package org.myproject.support.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.myproject.support.logsession.SessionMBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class SessionExpiredListener implements HttpSessionListener {

    private String username;
    
    private Date startDate;
    
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        System.out.println("Create Logout Listener ");
        // TODO Auto-generated method stub
        
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        
        System.out.println("Destroy Logout Listener " + event.toString());
        
        try {
            HttpSession session = event.getSession();
            
            if (session != null) {
                
                String strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(session.getCreationTime()));

                System.out.println("Logout Listener username : " + session.getAttribute("username"));
                System.out.println("Logout Listener lastlogin : " + new Date(session.getLastAccessedTime()));
                System.out.println("Logout Listener created : " + strDate);
                System.out.println("Logout Listener inative : " + session.getMaxInactiveInterval() + " segundos");
                
                username = session.getAttribute("username").toString();
                startDate = new Date(session.getLastAccessedTime());
                
                this.sessionEnd(event);
            }
            
        } catch (Exception e) {
            System.out.println("Logout Listener Exception : " + e.getMessage());
        } finally {
            ;
        }
        
    }
    
    
    public void sessionEnd (HttpSessionEvent event) {
        
        HttpSession session = event.getSession();
        
        ApplicationContext ctx =  WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        
        SessionMBean sessionMBean = (SessionMBean) ctx.getBean("sessionMBean");

        sessionMBean.setPollStop(true);
        sessionMBean.setPollRender(false);

        
        sessionMBean.sessionEnd(this.username, this.startDate);
        sessionMBean.gotoURL("/public/public");
        
    }
    

}
