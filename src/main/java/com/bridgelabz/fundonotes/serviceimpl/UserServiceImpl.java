package com.bridgelabz.fundonotes.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.fundonotes.dto.LoginDTO;
import com.bridgelabz.fundonotes.dto.ValidateUser;
import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.repository.UserRepository;
import com.bridgelabz.fundonotes.service.UserService;
import com.bridgelabz.fundonotes.util.Utility;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	Utility util;

	@Override
	@Transactional
	public String userRegistration(ValidateUser validateUser) {
		UserDetails userDetails = modelMapper.map(validateUser, UserDetails.class);
		userDetails.setCreateTime(LocalDateTime.now());
		userDetails.setUpdateTime(LocalDateTime.now());
		userDetails.setVarified(false);
		userRepository.save(userDetails);
		String token = util.jwtToken(userDetails.getId());
		if (userDetails != null) {
			token = util.jwtToken(userDetails.getId());
			String url = "http://localhost:8082/fundonote/verifyuser";
		}
		return token;
	}

	@Override
	@Transactional
	public List<ValidateUser> userLogin(LoginDTO loginDto) {
		return null;
	}

	@Override
	@Transactional
	public int userForgotPassword(ValidateUser validateUser) {
		return 0;
	}

	public String updateRegistration(String token) {
		String message = null;
		Integer userId = util.jwtTokenParser(token);
		if (userId != null) {
			UserDetails details = userRepository.getOne(userId);
			details.setVarified(true);
			userRepository.save(details);
			message = "Verified successfully";
		} else {
			message = "Not Verified ";
		}
		return message;
	}

}
