package com.bridgelabz.fundonotes.service;

import com.bridgelabz.fundonotes.dto.ValidateUser;

public interface UserService {

	abstract int userRegistration(ValidateUser validUser);
	abstract int userLogin(ValidateUser validUser);
	abstract int userForgotPassword(ValidateUser validUser);

}
