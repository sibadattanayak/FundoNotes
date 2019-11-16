package com.bridgelabz.fundonotes.dto;

public class UserLoginDTO {
	private String userEmail;
	private String userPassword;

	public String getUserEmail() {
		return userEmail;
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
	
	public UserLoginDTO() {
	}

	public UserLoginDTO(String userEmail, String userPassword) {

		this.userEmail = userEmail;
		this.userPassword = userPassword;
	}


}
