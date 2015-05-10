package org.myproject.test.conndb.repositories;

import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;







import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.ScientificField;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.RoleRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.repositories.ScientificFieldRepository;
import org.myproject.support.scientificfield.ScientificFieldMBean;
import org.myproject.test.conndb.AbstractDatabaseTest;


public class TeacherRepositoryTest extends AbstractDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(TeacherRepositoryTest.class);

	private Long t_id = 2L;
 
	@Inject
	private TeacherRepository teacherRepository;

	@Inject
	private ScientificFieldRepository scientificFieldRepository;

	@Inject
	private RoleRepository roleRepository;


	@Inject
	private ScientificFieldMBean mbSC;
	
	public void testTeacherRepository() {

		
		Teacher teacher = this.teacherRepository.findOne(t_id);
		
		LOGGER.info(teacher);
	}

	@Test
	public void testScientificFieldRepository() {

		List<ScientificField> teacher = this.scientificFieldRepository.findAll();
		
		for (ScientificField sf: teacher) {
			System.out.println("Id " + sf.getId());
			System.out.println("Descr " + sf.getDescription());
		}

		List<SelectItem> list = mbSC.getSelectOneItemsScientificField();
		
		for (SelectItem s: list) {
			System.out.println("Id " + s.getValue());
			System.out.println("Descr " + s.getLabel());
			
		}

	}

}
