package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.UserForgotPasswordValidation;
import com.bridgelabz.fundonotes.dto.UserLoginValidation;
import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.dto.ResetPasswordDTO;
import com.bridgelabz.fundonotes.dto.UserDataValidation;

public interface User {

	UserDetails userRegistration(UserDataValidation userDataValidation);

	String userLogin(UserLoginValidation loginDto);

	String userForgotPassword(String email);

	String userResetPassword(ResetPasswordDTO password, String token);

	String userVarification(String token);

	List<UserDetails> showUserList(UserDataValidation userDataValidation);

	List<UserDataValidation> showNoteColabratorList(UserDataValidation userDataValidation, String token);

}
