package org.myproject.test.conndb.repositories;

import java.util.List;

import javax.inject.Inject;





import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.Teacher;
import org.myproject.model.entities.TeacherHoursExecutionYear;
import org.myproject.model.repositories.TeacherHoursExecutionYearRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.test.conndb.AbstractDatabaseTest;


public class TeacherHoursExecutionYearRepositoryTest extends AbstractDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(TeacherHoursExecutionYearRepositoryTest.class);

	private Long t_id = 2L;
 
	@Inject
	private TeacherHoursExecutionYearRepository teacherRepository;


	public void testTeacherRepository() {

		
		List<TeacherHoursExecutionYear> teacher = this.teacherRepository.findByTeacherAndExecutionYear(1L, "2014/2015");
		
		LOGGER.info(teacher);
	}
	
	@Test
	public void testTeacherSurveyDoneRepository() {

		
		List<TeacherHoursExecutionYear> teacher = this.teacherRepository.findByTeacherSurveyNOTDoneInExecutionYear(3L, "2014/2015");
		
		LOGGER.info(teacher);
	}

}
