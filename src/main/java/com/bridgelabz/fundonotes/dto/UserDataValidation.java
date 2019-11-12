package com.bridgelabz.fundonotes.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.util.Utility;

public class UserDataValidation {

	public UserDataValidation() {
		
		
	}

	public UserDataValidation(@NotEmpty(message = "Please provide your first name") String firstName,
			@NotEmpty(message = "Please provide your last name") String lastName,
			@Email @NotEmpty(message = "Please provide your email") String email,
			@NotEmpty(message = "Please provide your password") @Size(min = 3, max = 10, message = "Your password must have at least 3-10 characters") String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	@NotEmpty(message = "Please provide your first name")
	private String firstName;

	@NotEmpty(message = "Please provide your last name")
	private String lastName;

	@Email
	@NotEmpty(message = "Please provide your email")
	private String email;

	@NotEmpty(message = "Please provide your password")
	@Size(min = 3, max = 10, message = "Your password must have at least 3-10 characters")
	private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
