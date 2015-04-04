package org.myproject.test.conndb.repositories;

import java.util.List;

import javax.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.ProfessorshipRepository;
import org.myproject.model.repositories.RoleRepository;
import org.myproject.model.repositories.TeacherRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.test.conndb.AbstractDatabaseTest;
import org.springframework.beans.factory.annotation.Autowired;

public class TeacherNameRepositoryTest extends AbstractDatabaseTest {

    private static final Logger LOGGER = Logger.getLogger(TeacherNameRepositoryTest.class);

    @PersistenceContext
    EntityManager em;

    @Inject
    TeacherRepository teacherRepository;

    @Inject
    ProfessorshipRepository professorshipRepository;

    public void testTeacherByNameRepository1() {

        List<Teacher> teachers = em.createNamedQuery("Teacher.findAllTeachers", Teacher.class).getResultList();

        for (Teacher te : teachers) {
            System.out.println("Category : " + te.getCategory());
            System.out.println("Name : " + te.getFullName());
        }

        // LOGGER.info(teachers);
    }

    public void testTeacherByNameRepository2() {

        List<Object[]> teachers = em.createNamedQuery("Teacher.findCatNameTeachers").getResultList();

        for (Object[] te : teachers) {
            System.out.println("Category : " + te[0]);
            System.out.println("Name     : " + te[1]);
        }

        // LOGGER.info(teachers);
    }

    
    public void filterTest4() {
        Teacher teacher = teacherRepository.findOne(40L);

        LOGGER.info(teacher);
    }

    @Test
    public void filterTest5() {
        List <Teacher> teacher = teacherRepository.findByExecutionYear("2014/2015");

        
        //LOGGER.info(teacher);
        LOGGER.info(teacher.size());
        
    }

}
