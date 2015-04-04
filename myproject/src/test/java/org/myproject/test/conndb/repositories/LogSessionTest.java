package org.myproject.test.conndb.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.myproject.model.entities.LogSession;
import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.LogSessionRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.test.conndb.AbstractDatabaseTest;


public class LogSessionTest  extends AbstractDatabaseTest {

    @Inject
    private UserRepository userRepository;
    
    @Inject
    private LogSessionRepository sessionRepository;
    
    
    
    public void Test1 () {
        System.out.println("Passou");

        LogUser user = this.userRepository.findByUserName("admin");
        System.out.println("Passou   " + user.getId());

        Long session = this.sessionRepository.findIdByUserAndIdActive(user.getId());
        
        System.out.println("Passou");
        System.out.println("Sessão : " + session);
   }
    

    public void Test2 () {
        System.out.println("Passou");
        
        String oldstring = "2015-02-21 21:31:08";
        Date date;
        
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(oldstring);
            LogUser user = this.userRepository.findByUserName("admin");
            System.out.println("Passou   " + user.getId());

            Long session = this.sessionRepository.findIdByUserAndStartDateAndIdActive(user.getId(), date);
            
            System.out.println("Passou");
            System.out.println("Sessão : " + session);
            
       } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
   }


    public void Test3 () {
        System.out.println("Passou");
        
        List<LogSession> sessions = this.sessionRepository.findAll();
        
        for ( LogSession s: sessions) {
            System.out.println("#          : " + s.getId());
            System.out.println("Estado     : " + s.getActive());
            System.out.println("Utilizador : " + s.getUser().getTeacher().getFullName());
            System.out.println("Início     : " + s.getStartDate());
            System.out.println("Fim        : " + s.getEndDate());
            
        }
    }
    
    @Test
    public void Test4 () {
        System.out.println("Passou");
        
        List<LogSession> sessions = this.sessionRepository.findAllActiveSessions();
        
        for ( LogSession s: sessions) {
            
            this.sessionRepository.deleteAllUserZombiesSessions(s.getUser().getId(), s.getId());
            System.out.println("#          : " + s.getId());
            System.out.println("Estado     : " + s.getActive());
            System.out.println("Utilizador : " + s.getUser().getTeacher().getFullName());
            System.out.println("Início     : " + s.getStartDate());
            System.out.println("Fim        : " + s.getEndDate());
            
        }
    }

}
