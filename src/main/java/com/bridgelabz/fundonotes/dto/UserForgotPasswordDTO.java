package com.bridgelabz.fundonotes.dto;

public class UserForgotPasswordDTO {
	private String password;
	private String confirmPassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConformPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public UserForgotPasswordDTO() {

	}

	public UserForgotPasswordDTO(String password, String confirmPassword) {
		this.password = password;
		this.confirmPassword = confirmPassword;
	}

}
