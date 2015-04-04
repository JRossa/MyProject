package org.myproject.dao;

import org.myproject.model.utils.Hibernate;

import org.hibernate.Session;
import org.myproject.model.entities.LogUser;



public class UserDaoImpl implements UserDao {

	@Override
	public LogUser findbyUser(LogUser logUser) {
		LogUser user = null;
		Session session = Hibernate.getSessionFactory().getCurrentSession();
		String queryString = "";
		
		try {
			session.beginTransaction();
			user = (LogUser) session.createSQLQuery(queryString);
			session.beginTransaction().commit();
		} catch (Exception e) {
			session.beginTransaction().rollback();
		}
		return user;
	}

	@Override
	public LogUser myLogin(LogUser logUser) {
		// TODO Auto-generated method stub
		return null;
	}

}
