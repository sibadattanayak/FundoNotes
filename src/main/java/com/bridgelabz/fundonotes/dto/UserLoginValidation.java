package com.bridgelabz.fundonotes.dto;

public class UserLoginValidation {
	private String userEmail;
	private String userPassword;

	public String getUserEmail() {
		return userEmail;
	}

	public UserLoginValidation(String userEmail, String userPassword) {
		super();
		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

}
