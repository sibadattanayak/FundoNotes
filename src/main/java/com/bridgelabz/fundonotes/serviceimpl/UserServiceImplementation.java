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

	private UserDetails userDetails;

	@Autowired
	private UserNoteRepository userNoteRepository;

	String message = null;

	private static Logger logger = Logger.getLogger(UserServiceImplementation.class);
	
	static {
		PropertyConfigurator
				.configure("/home/admin1/Desktop/SpringWorkspace/FundoNotes/src/main/resources/log4j.properties");
	}

	@Override
	@Transactional
	public UserDetails userRegistration(UserDataValidation userDataValidation) {
		userDetails = modelMapper.map(userDataValidation, UserDetails.class);
		userDetails.setPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));
		userDetails.setCreateTime(LocalDateTime.now());
		userDetails.setUpdateTime(LocalDateTime.now());
		userDetails.setVarified(false);
		userDetails = userDataRepository.save(userDetails);
		String token = null;
		if (userDetails != null) {
			token = util.jwtToken(userDetails.getUserId());
			String url = "http://localhost:8081/fundonotes/verifyuser/";
			util.javaMail(userDetails.getEmail(), token, url);
		}
		logger.info("user registration");
		return userDetails;
	}

	@Override
	@Transactional
	public String userLogin(UserLoginValidation loginDto) {
		String token = null;
		Optional<UserDetails> userDetails = userDataRepository.findByEmail(loginDto.getUserEmail());
		System.out.println(userDetails.get().isVarified());
		if (userDetails.isPresent()) {
			System.out.println("password matching : "
					+ bCryptPasswordEncoder.matches(loginDto.getUserPassword(), userDetails.get().getPassword()));
			if (userDetails.get().isVarified()
					&& bCryptPasswordEncoder.matches(loginDto.getUserPassword(), userDetails.get().getPassword())) {
				token = util.jwtToken(userDetails.get().getUserId());
			}
		} else
			token = "user not verified or invalid user";
		return token;
	}

	@Override
	@Transactional
	public String userForgotPassword(String email) {
		Optional<UserDetails> userDetails = userDataRepository.findByEmail(email);
		if (userDetails.isPresent()) {
			String token = util.jwtToken(userDetails.get().getUserId());
			util.javaMail(userDetails.get().getEmail(), token, "http://localhost:4200/forgotpassword/:");
			message = "Sent to email";
		} else
			message = "Failed to send ";
		return message;
	}

	@Override
	public String userVarification(String token) {
		String message = null;
		Long userId = util.jwtTokenParser(token);
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
	public String userResetPassword(String password, String token) {

		Long userId = util.jwtTokenParser(token);
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
		Long userId = util.jwtTokenParser(token);
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
		Long userId = util.jwtTokenParser(token);
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
		Long userId = util.jwtTokenParser(token);
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

		return null;
	}

	@Override
	public List<UserDetails> showUserList(UserDataValidation userDataValidation) {
//		List<UserDetails> userValidation = userDataRepository.findByFirstName();
		return null;
	}

	@Override
	public List<UserNoteLabelValidation> showNoteLabelList(UserNoteLabelValidation validateNoteLabel) {
		return null;
	}

	@Override
	public void createLabel(UserNoteLabelValidation noteLabelValidation, String token) {
	}

	@Override
	public void updateLabel(UserNoteLabelValidation noteLabelValidation, String token) {
	}

	@Override
	public void deleteLabel(UserNoteLabelValidation noteLabelValidation, String token) {
	}

	@Override
	public List<UserDataValidation> showNoteColabratorList(UserDataValidation userDataValidation, String token) {
		return null;
	}
}