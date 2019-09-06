package com.bridgelabz.fundonotes.service;

import com.bridgelabz.fundonotes.dto.ForgotPasswordDTO;
import com.bridgelabz.fundonotes.dto.LoginDTO;
import com.bridgelabz.fundonotes.dto.ValidateUser;

public interface UserService {

	abstract String userRegistration(ValidateUser validateUser);

	abstract String userLogin(LoginDTO loginDto);

	abstract String userForgotPassword(String email);

	abstract String userResetPassword(ForgotPasswordDTO password, String token);

}
