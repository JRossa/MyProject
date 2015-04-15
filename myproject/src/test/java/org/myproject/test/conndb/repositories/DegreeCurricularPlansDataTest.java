package org.myproject.test.conndb.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.myproject.model.entities.DegreeCurricularPlansData;
import org.myproject.model.entities.LessonPlan;
import org.myproject.model.entities.LogSession;
import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.Professorship;
import org.myproject.model.repositories.DegreeCurricularPlansDataRepository;
import org.myproject.model.repositories.LessonPlanRepository;
import org.myproject.model.repositories.LogSessionRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.test.conndb.AbstractDatabaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DegreeCurricularPlansDataTest  extends AbstractDatabaseTest {

    @Inject
    private DegreeCurricularPlansDataRepository degreeCurricularPlansDataRepository;
    
    static Logger logger = LoggerFactory.getLogger(DegreeCurricularPlansDataTest.class);
    
    
    @Test
    public void Test1 () {
       
        List<DegreeCurricularPlansData> degreeCPD = this.degreeCurricularPlansDataRepository.findAll();

        
        for (DegreeCurricularPlansData degree: degreeCPD) {
            System.out.println("#                    : " + degree.getId());
            System.out.println("Curso                : " + degree.getDegree().getName());
            System.out.println("UC                   : " + degree.getCourse().getName());
            System.out.println("Ano                  : " + degree.getCurricularYear());
            System.out.println("Semestre             : " + degree.getCurricularSemester());
            System.out.println("Início               : " + degree.getStartExecutionYear().getExecutionYear());
            if (degree.getEndExecutionYear() != null) {
            	System.out.println("Fim                  : " + degree.getEndExecutionYear().getExecutionYear().toString());
            	break;
            } else {
            	System.out.println("Fim                  : null" );
            }
        }
    }

}
