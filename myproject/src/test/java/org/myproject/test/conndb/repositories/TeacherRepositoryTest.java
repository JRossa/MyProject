package org.myproject.test.conndb.repositories;

import java.util.List;

import javax.inject.Inject;



import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.test.conndb.AbstractDatabaseTest;


public class TeacherRepositoryTest extends AbstractDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(TeacherRepositoryTest.class);

	private Long t_id = 2L;
 
	@Inject
	private TeacherRepository teacherRepository;

	@Test
	public void testTeacherRepository() {

		
		Teacher teacher = this.teacherRepository.findOne(t_id);
		
		LOGGER.info(teacher);
	}
}
