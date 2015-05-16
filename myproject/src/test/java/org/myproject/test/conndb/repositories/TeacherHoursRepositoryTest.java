package org.myproject.test.conndb.repositories;

import java.util.List;

import javax.inject.Inject;







import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherHours;
import org.myproject.model.entities.TeacherHoursExecutionYear;
import org.myproject.model.repositories.TeacherHoursExecutionYearRepository;
import org.myproject.model.repositories.TeacherHoursRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.test.conndb.AbstractDatabaseTest;


public class TeacherHoursRepositoryTest extends AbstractDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(TeacherHoursRepositoryTest.class);

	private Long t_id = 2L;
 
	@Inject
	private TeacherHoursRepository teacherRepository;

	@Test
	public void testTeacherRepository() {

		
		List<TeacherHours> teacher = this.teacherRepository.findByTeacherAndExecutionYear(1L, "2014/2015");
		
		LOGGER.info(teacher);
	}
	

	public void testTeacherSurveyDoneRepository() {

		
		List<TeacherHours> teacher = this.teacherRepository.findByCourseSurveyDoneInExecutionYear(3L, "2014/2015");
		
		LOGGER.info(teacher);
	}

}
