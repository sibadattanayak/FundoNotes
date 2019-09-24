package com.bridgelabz.fundonotes.dto;

public class UserForgotPasswordValidation {
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
}
