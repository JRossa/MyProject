package org.myproject.test.conndb.repositories;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.model.entities.LogUser;
import org.myproject.model.entities.Teacher;
import org.myproject.model.repositories.RoleRepository;
import org.myproject.model.repositories.UserRepository;
import org.myproject.support.user.UserMBean;
import org.myproject.test.conndb.AbstractDatabaseTest;

import com.sun.jna.platform.win32.Sspi.TimeStamp;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"/ctx-myproject-test-conndb.xml"})
//@Transactional
//@TransactionConfiguration(defaultRollback=false)
public class UserLoginRepositoryTest extends AbstractDatabaseTest {

    @PersistenceContext
    EntityManager               em;

    // @Autowired
    @Inject
    UserRepository              userRepository;

    @Inject
    RoleRepository              roleRepository;

    private static final Logger LOGGER = Logger.getLogger(UserLoginRepositoryTest.class);

    public UserLoginRepositoryTest() {

    }

   
    public void filterTest1() {

        TypedQuery<Teacher> query = em.createQuery("" + "select p from Teacher p " + "where p.id = 20", Teacher.class);

        List<Teacher> teachers = query.getResultList();

        for (Teacher t : teachers) {
            System.out.println(t.getFullName());
        }
    }

    @SuppressWarnings("unchecked")
    public void filterTest2() {

        List<Object[]> teachers = em.createNamedQuery("teacher.CategoryName").getResultList();

        for (Object[] t : teachers) {
            System.out.println("Category  : " + t[0]);
            System.out.println("Name      : " + t[1]);
            System.out.println("Name      : " + t[2]);
            System.out.println("Name      : " + t[3]);
        }
    }

    @Test
    public void filterTest3() {
        LogUser user = userRepository.findByUserNameAndRndPassword("admin", "admin");

        LOGGER.debug("Início do Debug");
        LOGGER.debug("Debug - ");
        LOGGER.info("Info " + user);
        LOGGER.error("Error - " + user);
        LOGGER.fatal("Fatal " + user);
        LOGGER.trace("trace");

        System.out.println(user.getLogRole().getRolename());
        LOGGER.info(user);
    }

    public void filterTest3a() {
        // $post_array['work_time'] =
        // date("Y-m-d H:i:s",(STRTOTIME($post_array['time_ended']) -
        // STRTOTIME($post_array['time_started'])) )
        LogUser user = userRepository.findByUserName("admin");

        System.out.printf("%s %tB %<te, %<tY\n", "Due date:", new Date());

        LOGGER.info(new Date());
        // user.setCreationDate(new Date());
        user.setLastLoginDate(user.getModificationDate());
        user.setModificationDate(new Date());
        // System.out.println(user.getLogRole().getRolename());
        // LOGGER.info(user);
    }

    public void filterTest4() {
        List<LogUser> users = userRepository.findAll();

        for (LogUser u : users) {

            LOGGER.info("FistName       : " + u.getFirstName());

            LOGGER.info("Role Name      : " + u.getLogRole().getRolename());
            LOGGER.info("CreationUser   : " + u.getCreationUser());
            LOGGER.info("ModUser        : " + u.getModificationUser());
            LOGGER.info("ModDate        : " + u.getModificationDate());
            LOGGER.info("Teacher Name   : " + u.getTeacher().getFullName());
        }
        // List <LogRole> roles = roleRepository.findAll();
        //
        // for(LogRole r: roles){
        // LOGGER.info("Role Name 		: " + r.getRolename());
        //
        // }
    }

 
}
