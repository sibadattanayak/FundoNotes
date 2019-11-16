package com.bridgelabz.fundonotes.dto;

public class UserResetPasswordDTO {
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserResetPasswordDTO() {

	}

	public UserResetPasswordDTO(String password) {
		this.password = password;
	}

}
