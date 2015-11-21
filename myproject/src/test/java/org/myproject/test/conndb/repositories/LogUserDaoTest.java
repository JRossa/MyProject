package org.myproject.test.conndb.repositories;

import org.myproject.dao.LessonPlanUser;
import org.myproject.dao.LogUserDao;

import java.text.ParseException;
import java.util.ArrayList;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.myproject.test.conndb.AbstractDatabaseTest;


//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"/ctx-myproject-test-conndb.xml"})
//@Transactional
//@TransactionConfiguration(defaultRollback=false)
public class LogUserDaoTest extends AbstractDatabaseTest {
	

	private static final Logger LOGGER = Logger.getLogger(LogUserDaoTest.class);
	
    @Inject
	LogUserDao user;
	
	
	@Test
	public void DaoTest () throws ParseException {
 	       
		String sessionId = "Nv1bajbbM9ocz24ZEBCeU0yF";
				
		user.autenticateUser("916655799", "54P6RA%hx0", sessionId);
		
//		ArrayList<LessonPlanUser> lstLessonPlan = user.getLessonPlanData(sessionId);
		
//		System.out.println(user.setData(sessionId, lstLessonPlan.get(2).getTitle(), "Summary"));
		
//		user.setUser(sessionId);

    }

}




















