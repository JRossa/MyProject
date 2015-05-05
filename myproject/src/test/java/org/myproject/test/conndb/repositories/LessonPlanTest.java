package org.myproject.test.conndb.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.joda.time.Days;
import org.joda.time.LocalDate;
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
    
    
    public String computeExecutionYear (Date date) {
        String executionYear = null;
 
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        
        SimpleDateFormat yearDate = new SimpleDateFormat("yyyy");
        SimpleDateFormat mounthDate = new SimpleDateFormat("MM");

        Integer intMounth = Integer.parseInt(mounthDate.format(date).toString());
        Integer intYear = Integer.parseInt(yearDate.format(date).toString());

        if (intMounth >= 10) {
        	executionYear = intYear + "/" + (intYear+1);
        } else {
        	executionYear = (intYear-1) + "/" + intYear;
        }

        return executionYear;
    }
    
    

    public void Test1 () {
        System.out.println("Passou");
        Long dayDelta = 20L;
        Long hourDelta = -2L;
        Long minuteDelta = -50L; 
        
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
          
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            
            Date date = new Date(lp.getStartDate().getTime() 
       		     + (dayDelta * 24 * 60 * 60 * 1000)
       		     + (hourDelta * 60 * 60 * 1000) 
       		     + (minuteDelta * 60 * 1000));
       
            System.out.println("Start Time : " + sdf.format(lp.getStartDate()));
            System.out.println("Shift Time : " + sdf.format(date));
            
            SimpleDateFormat yearDate = new SimpleDateFormat("yyyy");
            SimpleDateFormat mounthDate = new SimpleDateFormat("MM");

            System.out.println("Mes : " + mounthDate.format(date) + "   Ano : " + yearDate.format(date));

            String executionYear = null;
            Integer intMounth = Integer.parseInt(mounthDate.format(date).toString());
            Integer intYear = Integer.parseInt(yearDate.format(date).toString());
            
            System.out.println("Mes : " + intMounth + "   Ano : " + intYear);
           
            if (intMounth >= 10) {
            	executionYear = intYear + "/" + (intYear+1);
            } else {
            	executionYear = (intYear-1) + "/" + intYear;
            }
            
            System.out.println(executionYear);
            
        }
    }


    public void Test2 () {
        System.out.println("Passou");
        Long dayDelta = 20L;
        Long hourDelta = -2L;
        Long minuteDelta = -50L; 
        
        List<LessonPlan> lessonPlan = this.lessonPlanRepository.findAllMissing();

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
          
            Date date = new Date();
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            
            Date d1 = lp.getStartDate();
            Date d2 = date;
            
            Long diffTime = d2.getTime() - d1.getTime();
            Long numDays  =  diffTime / (24 * 60 * 60 * 1000);
            
            System.out.println("Start Time : " + sdf.format(lp.getStartDate()));
            System.out.println("End Time   : " + sdf.format(date));
            
            Days d = Days.daysBetween(new LocalDate(lp.getStartDate()), new LocalDate(date));
            
            System.out.println("Dias       : " + numDays + "         " + d.getDays());
        }
    }
    
    @Test   
    public void Test3 () {
    	System.out.println("ExecYear : " + computeExecutionYear(new Date()));
    }

}
