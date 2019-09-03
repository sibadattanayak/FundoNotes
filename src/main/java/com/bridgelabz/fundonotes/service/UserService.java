package com.bridgelabz.fundonotes.service;

import com.bridgelabz.fundonotes.model.UserDetails;

public interface UserService {

	abstract int userRegistration(UserDetails userDetails);

	abstract int userLogin(UserDetails userDetails);

	abstract int userForgotPassword(UserDetails userDetails);

}
