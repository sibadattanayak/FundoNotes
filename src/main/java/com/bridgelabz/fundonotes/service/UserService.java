package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.ValidateUser;

public interface UserService {

	abstract void userRegistration(ValidateUser validateUser);

	abstract List<ValidateUser> userLogin(ValidateUser validateUser);

	abstract int userForgotPassword(ValidateUser validateUser);

}
