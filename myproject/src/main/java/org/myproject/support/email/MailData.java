package org.myproject.support.email;



public class MailData {

	private String userLogin;
	
	private String userPassword;

	
	
	public MailData() {
		super();

	}

	public MailData(String userLogin, String userPassword) {
		super();
		this.userLogin = userLogin;
		this.userPassword = userPassword;
	}

	
	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
