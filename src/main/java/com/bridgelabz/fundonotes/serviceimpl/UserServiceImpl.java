package com.bridgelabz.fundonotes.serviceimpl;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.fundonotes.dto.ValidateUser;
import com.bridgelabz.fundonotes.repository.UserRepository;
import com.bridgelabz.fundonotes.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	UserRepository userRepository;
	@Override
	@Transactional
	public void userRegistration(ValidateUser validateUser) {
		 userRepository.registerNewUser(validateUser);

	}

	@Override
	@Transactional
	public List<ValidateUser> userLogin(ValidateUser validateUser) {
		return null;
	}

	@Override
	@Transactional
	public int userForgotPassword(ValidateUser validateUser) {
		return 0;
	}

}
