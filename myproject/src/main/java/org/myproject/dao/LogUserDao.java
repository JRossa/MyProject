package org.myproject.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



import org.myproject.model.entities.LogSession;
import org.myproject.model.entities.LogUser;

import org.myproject.model.utils.PasswordHash;
import org.myproject.model.utils.Stamp;


public class LogUserDao {

	
	private LessonPlanUser user = new LessonPlanUser();


	public LogUserDao() {
		super();
		
		this.user.setTeacherId(-1L);
		this.user.setUsername("");
		
	}

	
	private Long findByUserName(String username) {
		
		Long teacherId = -1L;
		
		try {
		
			Connection conn = Database.getConnection();
			
			String qry = "SELECT * FROM tbl_USER WHERE USER = ? AND ENABLED = TRUE";
			
			PreparedStatement ppStt;
		
			ppStt = conn.prepareStatement(qry);
			ppStt.setString(1, username);
			
			ResultSet rSet = ppStt.executeQuery();
			
			if (rSet.next()) {
				teacherId = rSet.getLong("ID");
			}
			
			ppStt.close();
			Database.close(conn);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return teacherId;
	}
	
	
	private Long findIdByUserAndIdActive(Long userId) {
		
		Long teacherId = -1L;
		
		try {
		
			Connection conn = Database.getConnection();
			
			String qry = "SELECT * FROM tbl_SESSION WHERE USER_ID = ? AND ACTIVE = TRUE";
			
			PreparedStatement ppStt;
		
			ppStt = conn.prepareStatement(qry);
			ppStt.setLong(1, userId);
			
			ResultSet rSet = ppStt.executeQuery();
			
			if (rSet.next()) {
				teacherId = rSet.getLong("ID");
			}

			ppStt.close();
			Database.close(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return teacherId;
	}
	
	
	private LogSession findOne(Long sessionId) throws ParseException {
		
		LogSession logSession = new LogSession();
		
		try {
		
			Connection conn = Database.getConnection();
			
			String qry = "SELECT * FROM tbl_SESSION WHERE ID = ?";
			
			PreparedStatement ppStt;
		
			ppStt = conn.prepareStatement(qry);
			ppStt.setLong(1, sessionId);

			System.out.println("QRY : " + ppStt.toString());
			
			ResultSet rSet = ppStt.executeQuery();
			
			if (rSet.next()) {
				logSession.setId(rSet.getLong("ID"));
				
				LogUser logUser = new LogUser();
				logUser.setId(rSet.getLong("USER_ID"));
				logSession.setUser(logUser);
				
				logSession.setActive(rSet.getBoolean("ACTIVE"));
				
				logSession.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rSet.getString("START_DATE")));
				logSession.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rSet.getString("END_DATE")));

				logSession.setSessionId(rSet.getString("SESSIONID"));
			}

			ppStt.close();
			Database.close(conn);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return logSession;
	}

	
	private void saveAndFlushLogSession(LogSession session) {

		Connection conn = Database.getConnection();

		System.out.println("saveAndFlush   " + session.getId());		
		System.out.println("saveAndFlush   " + session.getSessionId());		

		try {
			String qry;
			
			conn.setAutoCommit(false);
			
			if (session.getId() > 0 ) {
				qry = "UPDATE tbl_SESSION SET ACTIVE = ?, END_DATE = ?, SESSIONID = ?, "
						   + "START_DATE = ? , USER_ID = ?  "
						   + "WHERE ID = ?";
			} else {
				qry = "INSERT INTO tbl_SESSION (ACTIVE, END_DATE, SESSIONID, START_DATE, USER_ID) VALUES (?, ?, ?, ?, ?)";
			}
			
			PreparedStatement ppStt;
			ppStt = conn.prepareStatement(qry);
			
			ppStt.setBoolean(1, session.getActive());
			ppStt.setString(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(session.getEndDate()));
			ppStt.setString(3, session.getSessionId());
			ppStt.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(session.getStartDate()));
			ppStt.setLong(5, session.getUser().getId());
			
			if (session.getId() > 0 ) {
				ppStt.setLong(6, session.getId());
			}
			
			ppStt.executeUpdate();

			conn.commit();
			
			ppStt.close();
			Database.close(conn);
			
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}		

	}
	
	
    public void sessionStart(String username, String sessionId) throws ParseException {
    	
    	System.out.println("endSession username : " + username + "   sessionId : " + sessionId);
        LogUser logUser = new LogUser();
        
        logUser.setId(this.findByUserName(username));
        LogSession session = new LogSession();
       
        session.setId(-1L);
        session.setUser(logUser);
        session.setActive(true);
        session.setStartDate(new Date());
        session.setEndDate(new Date());
        session.setSessionId(sessionId);
        
        this.saveAndFlushLogSession(session);
        
      }

    
    public void sessionEnd(String username) {
    	 
    	if (username == null) {
    		return;
    	}
    	
    	System.out.println("endSession username : " + username);

    	LogUser logUser = new LogUser();
    	logUser.setId(this.findByUserName(username));
    	Long sessionId = this.findIdByUserAndIdActive(logUser.getId());
    	System.out.println("endSession username : " + username);

    	LogSession session = new LogSession();
    	
		try {
			session = this.findOne(sessionId);
	    	System.out.println("endSession username : " + session.getSessionId());
			
	    	session.setActive(false);  
	    	session.setEndDate(new Date());
	      
	    	this.saveAndFlushLogSession(session);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }


	public String autenticateUser(String mobilePhone, String passwd, String sessionId) {

		user.setTeacherId(-1L);
		
		try {
			
			Connection conn = Database.getConnection();
			
			String qry = "SELECT * FROM tbl_TEACHER WHERE MOBILE_PHONE = ?";
			
			PreparedStatement ppStt = conn.prepareStatement(qry);		
			ppStt.setString(1, mobilePhone);
			
			ResultSet rSet = ppStt.executeQuery();
			
			if (rSet.next()) {
				user.setTeacherId(rSet.getLong("ID"));
			}
			
			System.out.println("Id : " + user.getTeacherId());
			ppStt.close();
			
			if (user.getTeacherId() > 0) {

				qry = "SELECT * FROM tbl_USER WHERE TEACHER_ID = ? AND ENABLED = TRUE";
				
				ppStt = conn.prepareStatement(qry);
				ppStt.setLong(1, user.getTeacherId());
				
				System.out.println("QRY  : " + ppStt.toString());
				
				rSet = ppStt.executeQuery();
				
				if (rSet.next()) {
					String passwordStored = rSet.getString("PASSWORD");
					
					if (!PasswordHash.validatePassword(passwd, passwordStored)) {
						user.setTeacherId(-1L);
					} else {
						this.user.setUsername(rSet.getString("USER"));
						System.out.println("Username  : " + rSet.getString("USER"));
						
						this.sessionStart(this.user.getUsername(), sessionId);
					}
				}
				
			}
			
			ppStt.close();
			Database.close(conn);
			
		} catch (Exception e) {
			
		}

		if (user.getTeacherId() > 0) {
			return sessionId;
		} else {
			return user.getTeacherId().toString();
		}
			
	}
	
	
	private Long getUserId(String sessionId) {

		try {
			Connection conn = Database.getConnection();
			
			
			String qry = "SELECT * FROM tbl_SESSION WHERE SESSIONID = ? AND ACTIVE = TRUE";

			PreparedStatement ppStt;
			ppStt = conn.prepareStatement(qry);

			ppStt.setString(1, sessionId);
			
//			System.out.println("QRY  : " + ppStt.toString());
			
			ResultSet rSet = ppStt.executeQuery();
			
			if (rSet.next()) {
				return rSet.getLong("USER_ID");			
			}
			
			ppStt.close();
			Database.close(conn);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return -1L;
	}
	
	
	public void setUser(String sessionId) {
		
		
		user = loadData(sessionId);
		
		this.sessionEnd(user.getUsername());
		user.setTeacherId(-1L);
		
	}

	
	private LessonPlanUser loadData(String sessionId) {

		LessonPlanUser lpu = new LessonPlanUser();
		
		Long userId = getUserId(sessionId);
		
		lpu.setTeacherId(-1L);
		
		try {
			
			Connection conn = Database.getConnection();
			
			String qry = "SELECT * FROM tbl_USER WHERE ID = ? AND ENABLED= TRUE";

			PreparedStatement ppStt = conn.prepareStatement(qry);
			ppStt.setLong(1, userId);
			
			ResultSet rSet = ppStt.executeQuery();
			
			if (rSet.next()) {
				lpu.setTeacherId(rSet.getLong("TEACHER_ID"));
				lpu.setUsername(rSet.getString("USER"));
			}
			
			ppStt.close();
			Database.close(conn);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		return lpu;
	}

	
	public ArrayList<LessonPlanUser> getLessonPlanData(String sessionId) {
		
		ArrayList<LessonPlanUser> lstUser = new ArrayList<LessonPlanUser>();
		
		user = loadData(sessionId);
		
		if (user.getTeacherId() < 0) {
			return null;
		}
		
		try {
			
			Connection conn = Database.getConnection();
			
			String qry = "SELECT DISTINCT TITLE, PLACE FROM tbl_LESSON_PLAN_WEEKLY WHERE TEACHER_ID = ? AND ENABLED = TRUE";
			
			PreparedStatement ppStt = conn.prepareStatement(qry);		
			ppStt.setLong(1, user.getTeacherId());
			
			ResultSet rSet = ppStt.executeQuery();
			
			while (rSet.next()) {
				
				LessonPlanUser usr = new LessonPlanUser();

				usr.setTitle(rSet.getString("TITLE"));
				usr.setPlace(rSet.getString("PLACE"));
				
				System.out.println(usr.getTitle() + "     " + usr.getPlace());
				
				lstUser.add(usr);
			}
			
			ppStt.close();
			Database.close(conn);

		} catch (Exception e) {
			
		}

		return lstUser;
	}

	
	private String getCourseCode(Long courseId) {

		try {
			
			Connection conn = Database.getConnection();
			
			String qry = "SELECT * FROM tbl_COURSE WHERE ID = ?";
			
			PreparedStatement ppStt = conn.prepareStatement(qry);	
			ppStt.setLong(1, courseId);

			ResultSet rSet = ppStt.executeQuery();
			
			while (rSet.next()) {
				return rSet.getString("CODE") + " - WS ";
			}
			
			ppStt.close();
			Database.close(conn);
			
		} catch (Exception e) {
			
		}
		
		return "Web - Fail";
	}
	
	
	private void saveAndFlushLessonPlanUser(LessonPlanUser lessonPlanUser) {
		System.out.println("saveAndFlush  LessonPlanUser ");		
		
		Connection conn = Database.getConnection();
		
		try {
			
//			Connection conn = Database.getConnection();
			
			conn.setAutoCommit(false);
			
			String qry = "INSERT INTO tbl_LESSON_PLAN (TITLE, COURSE_ID, TEACHER_ID, DEGREE_ID, "
				       + "PLACE, START_DATE, END_DATE, DAYLIGHT, DESCRIPTION, LOCKED, "
				       + "CREATION_USER, CREATION_DATE, MODIFICATION_USER, MODIFICATION_DATE) "
				       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			PreparedStatement ppStt;
			ppStt = conn.prepareStatement(qry);
			
			ppStt.setString(1, lessonPlanUser.getTitle());
			ppStt.setLong(2, lessonPlanUser.getCourseId());
			ppStt.setLong(3, lessonPlanUser.getTeacherId());
			ppStt.setLong(4, lessonPlanUser.getDegreeId());
			ppStt.setString(5, lessonPlanUser.getPlace());
			ppStt.setString(6, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lessonPlanUser.getStartDate()));
			ppStt.setString(7, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lessonPlanUser.getEndDate()));
			ppStt.setInt(8, lessonPlanUser.getDaylight());
			ppStt.setString(9, lessonPlanUser.getDescription());
			ppStt.setBoolean(10, lessonPlanUser.getLocked());
			ppStt.setString(11, lessonPlanUser.getStamp().getCreationUser());
			ppStt.setString(12, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lessonPlanUser.getStamp().getCreationDate()));
			ppStt.setString(13, lessonPlanUser.getStamp().getModificationUser());
			ppStt.setString(14, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lessonPlanUser.getStamp().getModificationDate()));
			
			ppStt.executeUpdate();

			conn.commit();

			ppStt.close();
			Database.close(conn);
			
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}		

		
	}
	
	
	public Integer setData(String sessionId, String title, String lessonPlan) {
		ArrayList<LessonPlanUser> lstDegrees = new ArrayList<LessonPlanUser>();
		
		user = loadData(sessionId);
		
		if (user.getTeacherId() < 0) {
			return null;
		}
		System.out.println("setData " + sessionId + "   " + title);	
		
        TimeZone timezone = TimeZone.getTimeZone("Europe/Lisbon");

        Stamp stamp = new Stamp();
        
        stamp.setCreationUser(this.user.getUsername());
        stamp.setCreationDate(new Date());
        stamp.setModificationUser(this.user.getUsername());
        stamp.setModificationDate(new Date());
       
		try {
			
			Connection conn = Database.getConnection();
			
			String qry = "SELECT * FROM tbl_LESSON_PLAN_WEEKLY WHERE TITLE = ? AND TEACHER_ID = ? AND ENABLED = TRUE";
			
			PreparedStatement ppStt = conn.prepareStatement(qry);	
			ppStt.setString(1, title);
			ppStt.setLong(2, user.getTeacherId());
			
			ResultSet rSet = ppStt.executeQuery();
			
			while (rSet.next()) {
				
				LessonPlanUser usr = new LessonPlanUser();

				usr.setDegreeId(rSet.getLong("DEGREE_ID"));
				usr.setCourseId(rSet.getLong("COURSE_ID"));
				usr.setPlace(rSet.getString("PLACE"));
				usr.setDayOfWeek(rSet.getInt("DAY_OF_WEEK"));
				usr.setStartTime(rSet.getTime("START_TIME"));
				usr.setEndTime(rSet.getTime("END_TIME"));
				usr.setTitle(this.getCourseCode(usr.getCourseId()));
				
//				System.out.println(usr.getTitle() + "     " + usr.getPlace());
				
				lstDegrees.add(usr);
			}
			
			ppStt.close();
			Database.close(conn);

			for (LessonPlanUser lpu: lstDegrees) {
				System.out.println("Day of Week  : " + lpu.getDayOfWeek());
				System.out.println("Start Time  : " + lpu.getStartTime());
				System.out.println("EndTime  : " + lpu.getEndTime());
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeZone(timezone);
				calendar.setTime(new Date());
				
				lpu.setTeacherId(user.getTeacherId());
				
				if (lpu.getDayOfWeek() > calendar.get(Calendar.DAY_OF_WEEK)) {
					calendar.add(Calendar.WEEK_OF_YEAR, -1);
				} 
				
				calendar.set(Calendar.DAY_OF_WEEK, lpu.getDayOfWeek());
				
				// START_DATE
		        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
		        		lpu.getStartTime().getHours(), lpu.getStartTime().getMinutes(), 0);

		        lpu.setStartDate(calendar.getTime());

		        Long timeOffset = Integer.toUnsignedLong(timezone.getOffset(lpu.getStartDate().getTime())); 
		        calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
		        
		        lpu.setStartDate(calendar.getTime());

		        // END_DATE
		        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE),
		        		lpu.getEndTime().getHours(), lpu.getEndTime().getMinutes(), 0);

		        lpu.setEndDate(calendar.getTime());

		        calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
		        
		        lpu.setEndDate(calendar.getTime());
		        
		        lpu.setLocked(true);

		        Boolean daylight = timezone.inDaylightTime(lpu.getStartDate());
		        
		        if (daylight) {
		        	lpu.setDaylight(1);
		        } else {
		        	lpu.setDaylight(0);
		        }

		        System.out.println("Title  : " + lpu.getTitle());

		        System.out.println("StartDate  : " + lpu.getStartDate());
		        System.out.println("EndDate  : " + lpu.getEndDate());
		        System.out.println("CreationUser  : " + stamp.getCreationUser());
		        System.out.println("Creationdate  : " + stamp.getCreationDate());

		        lpu.setStamp(stamp);
		        
		        this.saveAndFlushLessonPlanUser(lpu);
			}
			
			return lstDegrees.size();
			
		} catch (Exception e) {
			
		}
		
		return -1;
	}
	
}
