package com.bridgelabz.fundonotes.serviceimpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.fundonotes.dto.ForgotPasswordDTO;
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
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional
	public String userRegistration(ValidateUser validateUser) {
		UserDetails userDetails = modelMapper.map(validateUser, UserDetails.class);
		userDetails.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		userDetails.setCreateTime(LocalDateTime.now());
		userDetails.setUpdateTime(LocalDateTime.now());
		userDetails.setVarified(true);
		userRepository.save(userDetails);
		String token = util.jwtToken(userDetails.getId());
		if (userDetails != null) {
			token = util.jwtToken(userDetails.getId());
			String url = "http://localhost:8082/fundonote/verifyuser";
			util.javaMail(userDetails.getEmail(), token, url);
		}
		return token;
	}

	@Override
	@Transactional
	public String userLogin(LoginDTO loginDto) {
		String token = null;
		Optional<UserDetails> userDetails = userRepository.findByEmail(loginDto.getUserEmail());
		if (userDetails.isPresent()) {
			if (userDetails.get().isVarified() == true
					&& bCryptPasswordEncoder.matches(loginDto.getUserPassword(), userDetails.get().getPassword())) {
				token = util.jwtToken(userDetails.get().getId());
			}
			return token;
		} else {
			return "BadCredentials!!!";
		}
	}

	@Override
	@Transactional
	public String userForgotPassword(String email) {
		LoginDTO loginDto = null;
		Optional<UserDetails> userDetails = userRepository.findByEmail(loginDto.getUserEmail());
		if (userDetails.isPresent()) {
			String token = util.jwtToken(userDetails.get().getId());
			util.javaMail(userDetails.get().getEmail(), token, "");
		}
		return "Sent to email";
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

	@Override
	public String userResetPassword(ForgotPasswordDTO password, String token) {
		String message = null;

		Integer userId = util.jwtTokenParser(token);
		if (userId != null) {
			UserDetails details = userRepository.getOne(userId);
			if (details.isVarified() == true) {
				details.setUpdateTime(LocalDateTime.now());
				userRepository.save(details);
				message = "Verified successfully";
			} else {
				message = "Not Verified ";
			}
		} else {
			message = "Not Verified ";
		}
		return message;
	}

}
