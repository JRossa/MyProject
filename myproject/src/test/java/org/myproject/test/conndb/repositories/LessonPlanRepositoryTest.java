package org.myproject.test.conndb.repositories;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.Course;
import org.myproject.model.entities.LessonPlan;
import org.myproject.model.entities.LogRole;
import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.CourseRepository;
import org.myproject.model.repositories.LessonPlanRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.model.utils.EncryptHash;
import org.myproject.model.utils.PasswordHash;
import org.myproject.model.utils.RandomPasswordGenerator;
import org.myproject.test.conndb.AbstractDatabaseTest;

public class LessonPlanRepositoryTest extends AbstractDatabaseTest {

    @Inject
    LessonPlanRepository      lessonPlanRepository;

    @Inject
    CourseRepository          courseRepository;

    @Inject
    private TeacherRepository teacherRepository;

    private static final Logger LOGGER = Logger.getLogger(LessonPlanRepositoryTest.class);
    
    
    public LessonPlanRepositoryTest() {

    }
    
    @Test
    public void save() {
        String msg = "Registo Inserido";
        LessonPlan lessonPlan = new LessonPlan();
 
        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.DAY_OF_YEAR, 1);
        Date endDate = calendar.getTime();
        
        lessonPlan.setStartDate(startDate);
        lessonPlan.setEndDate(endDate);
        
        LOGGER.debug("Passou");
        
        lessonPlan.setId(3L);
        lessonPlan.setTitle("Novo");
        Teacher teacher = this.teacherRepository.findOne(146L);
        lessonPlan.setTeacher(teacher);
                
        Course course = this.courseRepository.findOne(171L);
        lessonPlan.setCourse(course);

        LOGGER.debug("Passou");

        
        LessonPlan retLessonPlan = this.lessonPlanRepository.save(lessonPlan);
        
        System.out.printf("Log ID is %d and for returned account ID is %d\n", lessonPlan.getId(), retLessonPlan.getId());

    }


}
