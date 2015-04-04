package org.myproject.test.conndb.repositories;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.Course;
import org.myproject.model.entities.Professorship;
import org.myproject.model.repositories.ProfessorshipRepository;
import org.myproject.test.conndb.AbstractDatabaseTest;

public class ProfessorshipTest extends AbstractDatabaseTest {

    private static final Logger LOGGER = Logger.getLogger(ProfessorshipTest.class);

    @Inject
    private ProfessorshipRepository professorshipRepository;

    public void testProfessorshipIdRepository0() {

        Professorship professorship = this.professorshipRepository.findOne(14L);

        System.out.println(professorship.getCourse().getCode());
        System.out.println(professorship.getTeacher().getFullName());

    }

    public void testProfessorshipIdRepository1() {

        List<Professorship> professorship = this.professorshipRepository.findAll();

        LOGGER.info("num of users:" + professorship.size());
        for (Professorship next : professorship) {
            System.out.println(next.getCourse().getCode());
            System.out.println(next.getTeacher().getFullName());

        }
    }

    public void testProfessorshipIdRepository2() {

        Professorship professorship = this.professorshipRepository.findOne(151L);

        System.out.println(professorship.getCourse().getCode());
        System.out.println(professorship.getTeacher().getFullName());

    }

    public void testProfessorshipIdRepository3() {

        List<Professorship> professorship = this.professorshipRepository.findByCourseCode(151);

        LOGGER.info("num of users:" + professorship.size());
        for (Professorship next : professorship) {
            System.out.println(next.getCourse().getCode());
            System.out.println(next.getTeacher().getFullName());

        }
    }


    public void testProfessorshipIdRepository4() {

        List<Professorship> professorship = this.professorshipRepository.findByTeacherId(2L);

        LOGGER.info("num of users:" + professorship.size());
        for (Professorship next : professorship) {
            System.out.println(next.getCourse().getCode());
            System.out.println(next.getTeacher().getFullName());

        }
    }
    
    
    @Test
    public void testProfessorshipIdRepository5() {

        List<Object[]> course = this.professorshipRepository.findCoursesByUserAndExecutionYear(2L, "2014/2015");

        LOGGER.info("num of courses:" + course.size());
        
        LOGGER.info(course);
        

        for (Object[] c: course) {
            System.out.println("Passou   " + c[0].toString());
            System.out.println("Passou   " + c[1].toString());
            System.out.println("Passou   " + c[2].toString());
            //System.out.println(next.getCode());
            //System.out.println(next.getName());

        }
    }

}
