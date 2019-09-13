package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.UserForgotPasswordValidation;
import com.bridgelabz.fundonotes.dto.UserLoginValidation;
import com.bridgelabz.fundonotes.dto.UserDataValidation;

public interface User {

	String userRegistration(UserDataValidation userDataValidation);

	String userLogin(UserLoginValidation loginDto);

	String userForgotPassword(String email);

	String userVarification(String token);

	String userResetPassword(UserForgotPasswordValidation password, String token);

	List<UserDataValidation> showUserList(UserDataValidation userDataValidation);

	List<UserDataValidation> showNoteColabratorList(UserDataValidation userDataValidation, String token);

}
