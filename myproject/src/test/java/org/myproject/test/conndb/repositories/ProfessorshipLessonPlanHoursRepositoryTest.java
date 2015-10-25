package org.myproject.test.conndb.repositories;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;





















import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.Course;
import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherLessonPlanHours;
import org.myproject.model.repositories.CourseRepository;
import org.myproject.model.repositories.TeacherLessonPlanHoursRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.support.professorship.ProfessorshipCourseHours;
import org.myproject.support.professorship.ProfessorshipLessonPlanCourseHours;
import org.myproject.test.conndb.AbstractDatabaseTest;


public class ProfessorshipLessonPlanHoursRepositoryTest extends AbstractDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(ProfessorshipLessonPlanHoursRepositoryTest.class);

	private Long t_id = 2L;
 
	@Inject
	private TeacherLessonPlanHoursRepository teacherLessonPlanHoursRepository;

	@Inject
	private TeacherRepository teacherRepository;
	
	@Inject
	private CourseRepository courseRepository;

	public void testTeacherLessonPlanRepository() {

		
		List<TeacherLessonPlanHours> teacher = this.teacherLessonPlanHoursRepository.findAll();
		
		LOGGER.info(teacher);
	}
	
	

	public void testTeacherCourseHoursRepository() {
		
		Teacher teacher = new Teacher();
		Course course = new Course();
		
		Date startDate = new Date();
		Date endDate = new Date();

		List<ProfessorshipLessonPlanCourseHours> teacherLesssonPlanTotHours = new ArrayList<ProfessorshipLessonPlanCourseHours>();
		
		 		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		
		try {
			startDate = sdf.parse("01-10-2015 00:00:00");
			endDate = sdf.parse("30-10-2015 00:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        System.out.println("Start Time : " + sdf.format(startDate));
        System.out.println("End Time   : " + sdf.format(endDate));
		
        
        List<Object[]> teacherHours = 
            		this.teacherLessonPlanHoursRepository.findTeacherCoursesHoursBetweenStartDateAndEndDate(startDate, endDate);
/*
        List<Object[]> teacherHours = 
            		this.teacherLessonPlanHoursRepository.findTeacherCoursesHoursByTeacherBetweenStartDateAndEndDate(64L, startDate, endDate);
*/
		LOGGER.info(teacher);

        for (Object[] c : teacherHours) {
            System.out.println("TeacherId    :  " + c[0].toString());
            System.out.println("CourseId     :  " + c[1].toString());
            System.out.println("Hours        :  " + c[2].toString());

            teacher = teacherRepository.findOne(Long.parseLong(c[0].toString()));
            course = courseRepository.findOne(Long.parseLong(c[1].toString()));
            
            teacherLesssonPlanTotHours.add(new ProfessorshipLessonPlanCourseHours(teacher, course, startDate, endDate, Integer.parseInt(c[2].toString())));
        }

        // Verificação

        for (ProfessorshipLessonPlanCourseHours th : teacherLesssonPlanTotHours) {
            System.out.println("Nome    :  " + th.getTeacher().getFullName());
            System.out.println("Course    :  " + th.getCourse().getName());
            System.out.println("Start_Date    :  " + th.getStartDate());
            System.out.println("End_Date    :  " + th.getEndDate());
            System.out.println("Hours    :  " + th.getHours());
            
        }
 
	
	}

    @Test
	public void testHoursRepository() {
  
    Date startDate = new Date();
    Calendar calendar = Calendar.getInstance();	
    Date endDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
    TimeZone timezone = TimeZone.getTimeZone("Europe/Lisbon");

 	try {
		startDate = sdf.parse("01-10-2015 00:00:00");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
    Boolean daylight = timezone.inDaylightTime(startDate);

    Long timeOffset = daylight ? (60 * 60 * 1000) : 0L;
    
    calendar.setTime(startDate);
    calendar.setTimeZone(timezone);
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
    		      0, 0, 0);
   
   	calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
   	
   	startDate = calendar.getTime();
   	System.out.println("Date   :  " + startDate + "   " + timeOffset + "   " + daylight);
    }
    
	
}
