package org.myproject.webservice;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.myproject.dao.LessonPlanUser;

@WebService
public interface LessonPlanManagementWS {
	

	@WebMethod
	String getAuthentication (@WebParam(name="mobilePhone") String mobilePhone, @WebParam(name="passwd") String passwd);

	@WebMethod
	void setFinished(@WebParam(name="sessionId") String sessionId);

	@WebMethod
	ArrayList<LessonPlanUser> getData(@WebParam(name="sessionId") String sessionId);
	
	@WebMethod
	Integer setData(@WebParam(name="sessionId") String sessionId, @WebParam(name="title") String title, @WebParam(name="lessonPlan") String lessonPlan);
	
}
