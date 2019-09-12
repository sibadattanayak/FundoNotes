package com.bridgelabz.fundonotes.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.fundonotes.dto.ForgotPasswordDTO;
import com.bridgelabz.fundonotes.dto.LoginDTO;
import com.bridgelabz.fundonotes.dto.NoteDTO;
import com.bridgelabz.fundonotes.dto.NoteLabelDTO;
import com.bridgelabz.fundonotes.dto.ValidateUser;
import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.repository.UserRepository;
import com.bridgelabz.fundonotes.service.UserNoteLabelService;
import com.bridgelabz.fundonotes.service.UserService;
import com.bridgelabz.fundonotes.util.Utility;

@Service
public class UserServiceImpl implements UserService, UserNoteLabelService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	Utility util;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	static {
		PropertyConfigurator
				.configure("/home/admin1/Desktop/SpringWorkspace/FundoNotes/src/main/resources/log4j.properties");
	}

	@Override
	@Transactional
	public String userRegistration(ValidateUser validateUser) {
		UserDetails userDetails = modelMapper.map(validateUser, UserDetails.class);
		userDetails.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		userDetails.setCreateTime(LocalDateTime.now());
		userDetails.setUpdateTime(LocalDateTime.now());
		userDetails.setVarified(false);
		userRepository.save(userDetails);
		String token = null;
		if (userDetails != null) {
			token = util.jwtToken(userDetails.getUserId());
			String url = "http://localhost:8082/fundonote/verifyuser/";
			util.javaMail(userDetails.getEmail(), token, url);
		}
		logger.info("user registration");
		return token;
	}

	@Override
	@Transactional
	public String userLogin(LoginDTO loginDto) {
		String token = null;
		Optional<UserDetails> userDetails = userRepository.findByEmail(loginDto.getUserEmail());
		if (userDetails.isPresent() && userDetails.get().isVarified() == true
				&& bCryptPasswordEncoder.matches(loginDto.getUserPassword(), userDetails.get().getPassword())) {
			token = util.jwtToken(userDetails.get().getUserId());
		}
		return token;
	}

	@Override
	@Transactional
	public String userForgotPassword(String email) {
		LoginDTO loginDto = null;
		Optional<UserDetails> userDetails = userRepository.findByEmail(loginDto.getUserEmail());
		if (userDetails.isPresent()) {
			String token = util.jwtToken(userDetails.get().getUserId());
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

	@Override
	public List<ValidateUser> showUserList(ValidateUser validateUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NoteLabelDTO> showNoteLabelList(NoteLabelDTO validateNoteLabel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ValidateUser> showNoteColabratorList(ValidateUser validateUser) {
		// TODO Auto-generated method stub
		return null;
	}

	public void createNote(NoteDTO validNote) {
		// TODO Auto-generated method stub
		
	}

}
