package org.myproject.test.conndb.repositories;

import java.util.List;







import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.CategoryLookupTableRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.test.conndb.AbstractDatabaseTest;


public class TeacherCategoryRepositoryTest extends AbstractDatabaseTest {

	private static final Logger LOGGER = Logger.getLogger(TeacherCategoryRepositoryTest.class);

	@PersistenceContext
	EntityManager em;

        @Inject
        private TeacherRepository teacherRepository;

        @Inject
        private CategoryLookupTableRepository categoryLookupTableRepository;

	
	public void testTeacherRepository() {

		List <Object[]> teachers = em.createNamedQuery("Teacher.findAllTeachers").getResultList();
		
		for (Object[] te: teachers) {
			System.out.println("Category : " + te[0]);

		}
	}
	
        @Test
        public void testReplaceString () {
            
            String str = "2014_2015";
            
            str = str.replace("_", "/");
            
            System.out.println(str);
            
        }

        @Test
        public void testTeacherCategoryLookupTableRepository() {
            
            List <Teacher> teacher = teacherRepository.findAll();
            
            for (Teacher t: teacher) {
              String academicName = categoryLookupTableRepository.findAcademicNameByCategory(t.getCategory());
              if (academicName == null)
                System.out.println("Category  :  " + t.getCategory() + "   " + academicName);

            }
        }

}
