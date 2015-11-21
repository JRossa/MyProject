package org.myproject.test.conndb.repositories;

import org.myproject.model.entities.LogUser;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;
import org.myproject.model.utils.Hibernate;
import org.myproject.test.conndb.AbstractDatabaseTest;


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"/ctx-myproject-test-conndb.xml"})
//@Transactional
//@TransactionConfiguration(defaultRollback=false)
public class UserLoginHibernateTest extends AbstractDatabaseTest {
	

	private static final Logger LOGGER = Logger.getLogger(UserLoginHibernateTest.class);
	
	
	// @Inject    
	// @Autowired 
	// estão configurados para 'repositories' no 'ctx-myproject.xml' com:
	// <jpa:repositories base-package="org.myproject.model.repositories" />
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void HibernateTest () {
       Session session = Hibernate.getSessionFactory().openSession();
	       
//       session.beginTransaction();
	 
//       Department department = new Department("java");
//       session.save(department);
//	 
//       session.save(new Employee("Jakab Gipsz",department));
//       session.save(new Employee("Captain Nemo",department));
//	      
//       session.getTransaction().commit();
	 
       Query q = session.createQuery("From LogUser");
	                 
       List<LogUser> resultList = q.list();
       
       LOGGER.info("num of users:" + resultList.size());
       for (LogUser next : resultList) {
            LOGGER.info("next employee: " + next);
        }
	 
    }

}




















