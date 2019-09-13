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

import com.bridgelabz.fundonotes.dto.UserDataValidation;
import com.bridgelabz.fundonotes.dto.UserForgotPasswordValidation;
import com.bridgelabz.fundonotes.dto.UserLoginValidation;
import com.bridgelabz.fundonotes.dto.UserNoteLabelValidation;
import com.bridgelabz.fundonotes.dto.UserNoteValidation;
import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNotes;
import com.bridgelabz.fundonotes.repository.UserDataRepository;
import com.bridgelabz.fundonotes.repository.UserNoteRepository;
import com.bridgelabz.fundonotes.service.Label;
import com.bridgelabz.fundonotes.service.Note;
import com.bridgelabz.fundonotes.service.User;
import com.bridgelabz.fundonotes.util.Utility;

@Service
public class UserServiceImplementation implements User, Label, Note {

	@Autowired
	private UserDataRepository userDataRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Utility util;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserDetails userDetails;
	@Autowired
	private UserNoteRepository userNoteRepository;

	private static Logger logger = Logger.getLogger(UserServiceImplementation.class);
	static {
		PropertyConfigurator
				.configure("/home/admin1/Desktop/SpringWorkspace/FundoNotes/src/main/resources/log4j.properties");
	}

	@Override
	@Transactional
	public String userRegistration(UserDataValidation userDataValidation) {
		userDetails = modelMapper.map(userDataValidation, UserDetails.class);
		userDetails.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		userDetails.setCreateTime(LocalDateTime.now());
		userDetails.setUpdateTime(LocalDateTime.now());
		userDetails.setVarified(false);
		userDataRepository.save(userDetails);
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
	public String userLogin(UserLoginValidation loginDto) {
		String token = null;
		Optional<UserDetails> userDetails = userDataRepository.findByEmail(loginDto.getUserEmail());
		if (userDetails.isPresent() && userDetails.get().isVarified() == true
				&& bCryptPasswordEncoder.matches(loginDto.getUserPassword(), userDetails.get().getPassword())) {
			token = util.jwtToken(userDetails.get().getUserId());
		}
		return token;
	}

	@Override
	@Transactional
	public String userForgotPassword(String email) {
		UserLoginValidation loginDto = null;
		Optional<UserDetails> userDetails = userDataRepository.findByEmail(loginDto.getUserEmail());
		if (userDetails.isPresent()) {
			String token = util.jwtToken(userDetails.get().getUserId());
			util.javaMail(userDetails.get().getEmail(), token, "");
		}
		return "Sent to email";
	}

	@Override
	public String userVarification(String token) {
		String message = null;
		Integer userId = util.jwtTokenParser(token);
		if (userId != null) {
			UserDetails details = userDataRepository.getOne(userId);
			details.setVarified(true);
			userDataRepository.save(details);
			message = "Verified successfully";
		} else {
			message = "Not Verified ";
		}
		return message;
	}

	@Override
	public String userResetPassword(UserForgotPasswordValidation password, String token) {
		String message = null;

		Integer userId = util.jwtTokenParser(token);
		if (userId != null) {
			UserDetails details = userDataRepository.getOne(userId);
			if (details.isVarified() == true) {
				details.setUpdateTime(LocalDateTime.now());
				userDataRepository.save(details);
				message = "Verified successfully";
			} else {
				message = "Not Verified ";
			}
		} else {
			message = "Not Verified ";
		}
		return message;
	}

	public UserNotes createNote(String noteData, String token) {
		Integer userId = util.jwtTokenParser(token);
		UserNotes notes = null;
		if (userId != null) {
			Optional<UserDetails> userDetails = userDataRepository.findById(userId);
			if (userDetails.isPresent() && userDetails.get().isVarified()) {
				notes = modelMapper.map(noteData, UserNotes.class);
				notes.setNoteCreateTime(LocalDateTime.now());
				notes.setNoteUpdateTime(LocalDateTime.now());
				notes = userNoteRepository.save(notes);
			}
		}
		return notes;
	}

	@Override
	public UserNotes updateNote(Long noteId, String token) {
		Integer userId = util.jwtTokenParser(token);
		UserNotes notes = null;

		if (userId != null) {
			Optional<UserDetails> userDetails = userDataRepository.findById(userId);
			if (userDetails.isPresent() && userDetails.get().isVarified()) {
				Optional<UserNotes> noteModel = userNoteRepository.findById(noteId);
				noteModel.get().setNoteUpdateTime(LocalDateTime.now());
				notes = userNoteRepository.save(noteModel.get());
			}
		}
		return notes;
	}

	@Override
	public String deleteNote(Long noteId, String token) {
		Integer userId = util.jwtTokenParser(token);
		UserNotes notes = null;

		if (userId != null) {
			Optional<UserDetails> userDetails = userDataRepository.findById(userId);
			if (userDetails.isPresent() && userDetails.get().isVarified()) {
				userNoteRepository.deleteById(noteId);
			}
		}

		return "Deleted Successfully";
	}

	@Override
	public List<UserNoteValidation> showNoteList(UserNoteValidation validateNote) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserDataValidation> showUserList(UserDataValidation userDataValidation) {
		return null;
	}

	@Override
	public List<UserNoteLabelValidation> showNoteLabelList(UserNoteLabelValidation validateNoteLabel) {
		return null;
	}

	@Override
	public void createLabel(UserNoteLabelValidation noteLabelValidation, String token) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLabel(UserNoteLabelValidation noteLabelValidation, String token) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteLabel(UserNoteLabelValidation noteLabelValidation, String token) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<UserDataValidation> showNoteColabratorList(UserDataValidation userDataValidation, String token) {
		// TODO Auto-generated method stub
		return null;
	}

}