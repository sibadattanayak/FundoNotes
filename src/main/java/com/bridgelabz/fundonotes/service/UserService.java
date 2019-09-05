package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.LoginDTO;
import com.bridgelabz.fundonotes.dto.ValidateUser;

public interface UserService {

	abstract String userRegistration(ValidateUser validateUser);

	abstract List<ValidateUser> userLogin(LoginDTO loginDto);

	abstract int userForgotPassword(ValidateUser validateUser);

}
