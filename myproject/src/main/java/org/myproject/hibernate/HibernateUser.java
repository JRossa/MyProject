package org.myproject.hibernate;

import org.myproject.model.entities.LogUser;



public interface HibernateUser {

	public LogUser findbyUser(LogUser logUser);
	
	public LogUser myLogin(LogUser logUser);
}
