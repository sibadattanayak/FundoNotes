package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.ForgotPasswordDTO;
import com.bridgelabz.fundonotes.dto.LoginDTO;
import com.bridgelabz.fundonotes.dto.ValidateUser;

public interface UserService {

	String userRegistration(ValidateUser validateUser);

	String userLogin(LoginDTO loginDto);

	String userForgotPassword(String email);

	String userResetPassword(ForgotPasswordDTO password, String token);

	List<ValidateUser> showUserList(ValidateUser validateUser);

	List<ValidateUser> showNoteColabratorList(ValidateUser validateUser);

}
