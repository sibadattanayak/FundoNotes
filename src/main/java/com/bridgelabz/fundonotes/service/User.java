package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.UserResetPasswordDTO;
import com.bridgelabz.fundonotes.dto.UserInfoDTO;
import com.bridgelabz.fundonotes.dto.UserLoginDTO;
import com.bridgelabz.fundonotes.model.UserDetailsModel;

public interface User {

	UserDetailsModel userRegistration(UserInfoDTO userInfoDTO);

	String userLogin(UserLoginDTO loginDto);

	String userForgotPassword(String email);

	String userResetPassword(UserResetPasswordDTO password, String token);

	String userVarification(String token);

	List<UserDetailsModel> showUserList(String token);

}
