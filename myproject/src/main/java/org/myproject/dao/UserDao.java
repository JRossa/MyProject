package org.myproject.dao;

import org.myproject.model.entities.LogUser;



public interface UserDao {

	public LogUser findbyUser(LogUser logUser);
	
	public LogUser myLogin(LogUser logUser);
}
