package org.myproject.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

	public static Connection getConnection() {
		
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3308/dce", "root", "root");
			return con;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			System.out.println("Database.getConnection() Error -->"
					+ ex.getMessage());
			return null;
		} 
	
	    return null;
	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (Exception ex) {
		}
	}
}
