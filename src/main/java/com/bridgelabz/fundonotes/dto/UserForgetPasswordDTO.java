package com.bridgelabz.fundonotes.dto;

public class UserForgetPasswordDTO {
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserForgetPasswordDTO() {
	}

	public UserForgetPasswordDTO(String email) {
		this.email = email;
	}

}
