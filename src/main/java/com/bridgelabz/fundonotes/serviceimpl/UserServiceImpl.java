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
import com.bridgelabz.fundonotes.model.UserNotes;
import com.bridgelabz.fundonotes.repository.UserNotesRepository;
import com.bridgelabz.fundonotes.repository.UserRepository;
import com.bridgelabz.fundonotes.service.UserNoteLabelService;
import com.bridgelabz.fundonotes.service.UserNoteService;
import com.bridgelabz.fundonotes.service.UserService;
import com.bridgelabz.fundonotes.util.Utility;

@Service
public class UserServiceImpl implements UserService, UserNoteLabelService, UserNoteService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Utility util;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserDetails userDetails;
	@Autowired
	private UserNotesRepository userNotesRepository;

	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	static {
		PropertyConfigurator
				.configure("/home/admin1/Desktop/SpringWorkspace/FundoNotes/src/main/resources/log4j.properties");
	}

	@Override
	@Transactional
	public String userRegistration(ValidateUser validateUser) {
		userDetails = modelMapper.map(validateUser, UserDetails.class);
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

	public void createNote(String noteData, String token) {
		Integer userId = util.jwtTokenParser(token);
		UserNotes notes = null;
		if (userId != null) {
			Optional<UserDetails> userDetails = userRepository.findById(userId);
			if (userDetails.isPresent() && userDetails.get().isVarified()) {
				notes = modelMapper.map(noteData, UserNotes.class);
				notes.setNoteCreateTime(LocalDateTime.now());
				notes.setNoteUpdateTime(LocalDateTime.now());
				notes = userNotesRepository.save(notes);
			}
		}
	}

	@Override
	public void updateNote(int noteId, String token) {
		Integer userId = util.jwtTokenParser(token);
		UserNotes notes = null;

		if (userId != null) {
			Optional<UserDetails> userDetails = userRepository.findById(userId);
			if (userDetails.isPresent() && userDetails.get().isVarified()) {			
				  Optional<UserNotes> noteModel= userNotesRepository.findById(noteId);
				  noteModel.setNoteUpdateTime(LocalDateTime.now());  
				notes = userNotesRepository.save(noteModel);
			}
		}
	}

	@Override
	public void deleteNote(int noteId, String token) {
		Integer userId = util.jwtTokenParser(token);
		UserNotes notes = null;

		if (userId != null) {
			Optional<UserDetails> userDetails = userRepository.findById(userId);
			if (userDetails.isPresent() && userDetails.get().isVarified()) {
				notes = modelMapper.map(noteId, UserNotes.class);
				notes.setNoteUpdateTime(LocalDateTime.now());
				notes = userNotesRepository.deleteByNoteId(notes);

			}
		}
	}

	@Override
	public List<NoteDTO> showNoteList(NoteDTO validateNote) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ValidateUser> showUserList(ValidateUser validateUser) {
		return null;
	}

	@Override
	public List<NoteLabelDTO> showNoteLabelList(NoteLabelDTO validateNoteLabel) {
		return null;
	}

	@Override
	public List<ValidateUser> showNoteColabratorList(ValidateUser validateUser) {
		return null;
	}

}