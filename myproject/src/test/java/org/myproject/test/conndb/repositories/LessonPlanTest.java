package org.myproject.test.conndb.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.myproject.model.entities.LessonPlan;
import org.myproject.model.entities.LogSession;
import org.myproject.model.entities.LogUser;
import org.myproject.model.repositories.LessonPlanRepository;
import org.myproject.model.repositories.LogSessionRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.test.conndb.AbstractDatabaseTest;


public class LessonPlanTest  extends AbstractDatabaseTest {

    @Inject
    private LessonPlanRepository lessonPlanRepository;
    
    
    
    @Test
    public void Test1 () {
        System.out.println("Passou");
        int dayDelta = 2;
        int hourDelta = -2;
        int minuteDelta = -50; 
        
        List<LessonPlan> lessonPlan = this.lessonPlanRepository.findAll();

        for ( LessonPlan lp: lessonPlan) {
            System.out.println("#                    : " + lp.getId());
            System.out.println("Título               : " + lp.getTitle());
            System.out.println("Bloqueado            : " + lp.getLocked());
            System.out.println("Docente              : " + lp.getTeacher().getFullName());
            System.out.println("UC                   : " + lp.getCourse().getName());
            System.out.println("Início               : " + lp.getStartDate());
            System.out.println("Fim                  : " + lp.getEndDate());
            System.out.println("Descrição            : " + lp.getDescription());
            System.out.println("Create Date          : " + lp.getStamp().getCreationDate());
            System.out.println("Create User          : " + lp.getStamp().getCreationUser());
            System.out.println("Modification Date    : " + lp.getStamp().getModificationDate());
            System.out.println("Modification User    : " + lp.getStamp().getModificationUser());
          
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            
            Date date = new Date(lp.getStartDate().getTime() 
       		     + (dayDelta * 24 * 60 * 60 * 1000)
       		     + (hourDelta * 60 * 60 * 1000) 
       		     + (minuteDelta * 60 * 1000));
       
            System.out.println("Start Time : " + sdf.format(lp.getStartDate()));
            System.out.println("Shift Time : " + sdf.format(date));
            
        }
    }

}
