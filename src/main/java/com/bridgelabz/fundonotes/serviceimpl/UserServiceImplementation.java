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

import com.bridgelabz.fundonotes.dto.ResetPasswordDTO;
import com.bridgelabz.fundonotes.dto.UserDataValidation;
import com.bridgelabz.fundonotes.dto.UserLoginValidation;
import com.bridgelabz.fundonotes.dto.UserNoteLabelValidation;
import com.bridgelabz.fundonotes.dto.UserNoteValidation;
import com.bridgelabz.fundonotes.exception.CustomException;
import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNoteLabel;
import com.bridgelabz.fundonotes.model.UserNotes;
import com.bridgelabz.fundonotes.repository.UserDataRepository;
import com.bridgelabz.fundonotes.repository.UserNoteLabelRepository;
import com.bridgelabz.fundonotes.repository.UserNoteRepository;
import com.bridgelabz.fundonotes.service.Label;
import com.bridgelabz.fundonotes.service.Note;
import com.bridgelabz.fundonotes.service.User;
import com.bridgelabz.fundonotes.util.Utility;

@Service
public class UserServiceImplementation implements Label, User, Note {

	@Autowired
	private UserDataRepository userDataRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private Utility util;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private UserNoteRepository userNoteRepository;

	private UserDetails userDetails;
	private UserNoteValidation userNoteValidation;
	private UserNotes userNotes;
	private String message = null;

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
		logger.info("User Registered Successfully");
		String token = null;
		if (userDetails != null) {
			token = util.jwtToken(userDetails.getUserId());
			String url = "http://localhost:8081/fundonotes/verifyuser/";
			util.javaMail(userDetails.getEmail(), token, url);
			logger.info("varification mail is sent");
		}
		return userDetails;
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
			logger.info("User verified successfully");
		} else {
			logger.info("User couldn't verified");
			message = "Not Verified ";
		}
		return message;
	}

	@Override
	@Transactional
	public String userLogin(UserLoginValidation loginDto) {
		String token = null;
		Optional<UserDetails> userDetails = userDataRepository.findByEmail(loginDto.getUserEmail());
//		if (!userDetails.isPresent()) {
//			logger.info("Login attempted with unregistered email --> " + loginDto.getUserEmail());
//			throw new CustomException(404, "email does not exist");
//		}
		if (userDetails.get().isVarified()

				&& bCryptPasswordEncoder.matches(loginDto.getUserPassword(), userDetails.get().getPassword())) {
			token = util.jwtToken(userDetails.get().getUserId());
			logger.info("user logged in successfully");
		} else {
			logger.info("Login attempted");
			throw new CustomException(404, "user not verified or invalid user");
		}
		logger.info("Login attempted with email - " + loginDto.getUserEmail());
		return token;
	}

	@Override
	@Transactional
	public String userForgotPassword(String email) {
		Optional<UserDetails> userDetails = userDataRepository.findByEmail(email);
		if (userDetails.isPresent()) {
			String token = util.jwtToken(userDetails.get().getUserId());
			util.javaMail(userDetails.get().getEmail(), token, "http://localhost:4200/resetpassword/:");
			logger.info("API for forgotpassword has been sent to user's registered email");
			message = "Sent to email";
		} else {
			logger.info("API for forgotpassword couldn't sent");
			message = "Failed to send ";
			throw new CustomException(404, "Not found");
		}
		return message;
	}

	@Override
	public String userResetPassword(ResetPasswordDTO password, String token) {

		Long userId = util.jwtTokenParser(token);
		if (userId != null) {
			UserDetails details = userDataRepository.getOne(userId);
			if (details.isVarified() == true) {
				details.setUpdateTime(LocalDateTime.now());
				details.setPassword(bCryptPasswordEncoder.encode(password.getPasword()));
				userDataRepository.save(details);
				message = "Verified successfully";
				logger.info("User resetPassword successfully");
			} else {
				logger.info("ResetPassword unsuccessfully");
				message = "Not Verified ";
				throw new CustomException(106, "Unsuccessfull");
			}
		} else {
			logger.info("User token not found while resetting password");
			message = "Not Verified ";
			throw new CustomException(106, "Unsuccessfull");
		}
		return message;
	}

	public UserNotes createNote(UserNoteValidation userNoteDto, String token) {
		Long userId = util.jwtTokenParser(token);
		UserNotes notes = null;
		if (userId != null) {
			Optional<UserDetails> userDetails = userDataRepository.findById(userId);
			if (userDetails.isPresent() && userDetails.get().isVarified()) {
				notes = modelMapper.map(userNoteDto, UserNotes.class);
				notes.setNoteCreateTime(LocalDateTime.now());
				notes.setNoteUpdateTime(LocalDateTime.now());
				userDetails.get().getNotes().add(notes);
				logger.info("Note created successfully");
				return notes;
			} else
				logger.info("Note couldn't be created");
			throw new CustomException(106, "Note couldn't be created");
		}
		return notes;
	}

	@Override
	public UserNotes updateNote(UserNotes userNote, String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetails> userDetails = userDataRepository.findById(userId);
		logger.info("UserId for creating notes >> " + userId);
		Optional<UserNotes> notes = null;

		if (userDetails.isPresent() && userId != null) {
			notes = userNoteRepository.findById(userNote.getNoteId());
			if (notes.isPresent() && userDetails.get().isVarified()) {
				if (userNote.getNoteTitle() != null) {
					notes.get().setNoteTitle(userNoteValidation.getNoteTitle());
				} else {
					logger.info("User notes data not found");
					throw new CustomException(104, "Invalid User or User Not verified");
				}

				if (userNote.getNoteDescription() != null) {
					notes.get().setNoteDescription(userNoteValidation.getNoteDescription());

				} else {
					logger.info("User note not found for updating note");
					throw new CustomException(104, "Invalid User or NoteDescription is null");
				}
				notes.get().setNoteUpdateTime(LocalDateTime.now());
				userNoteRepository.save(notes.get());
			} else {
				throw new CustomException(104, "Invalid User or Token");
			}
		}
		return notes.get();
	}

	@Override
	public UserNotes deleteNote(Long noteId, String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetails> userDetails = userDataRepository.findById(userId);
		Optional<UserNotes> notes = null;

		if (userDetails.isPresent() && userId != null) {
			notes = userNoteRepository.findById(noteId);
			if (notes.isPresent() && userDetails.get().isVarified()) {
				userNoteRepository.deleteById(noteId);
				logger.info("User note deleted successfully");
			} else {
				logger.info("User not found or invalid token");
				throw new CustomException(104, "Invalid token or user not found");
			}
		} else {
			logger.info("User Details is not present or Token not found");
			throw new CustomException(104, "User Details not found");
		}
		return notes.get();
	}

	UserNoteLabelRepository labelRepository;

	@Override
	public UserNoteLabel createLabel(UserNoteLabelValidation labelDto, String token) {

		Long userId = util.jwtTokenParser(token);
		UserNoteLabel label = null;
		Optional<UserDetails> userDetails = userDataRepository.findById(userId);

		if (userId != null && userDetails.isPresent() && userDetails.get().isVarified()) {
			logger.info("User is present and verified");
			label = modelMapper.map(labelDto, UserNoteLabel.class);
			logger.info("DTO mapped with Model");
			label = labelRepository.save(label);
			logger.info("Label created successfully");
			return label;
		} else {
			logger.info("User Details not found of user not found");
			throw new CustomException(400, "User Details not found of user not found");
		}
	}

	@Override
	public UserNoteLabel updateLabel(UserNoteLabelValidation noteLabelValidation, String token) {

		Long userId = util.jwtTokenParser(token);
		UserNoteLabel label = null;
		Optional<UserDetails> userDetails = userDataRepository.findById(userId);

		if (userId != null && userDetails.isPresent() && userDetails.get().isVarified()) {
			Optional<UserNoteLabel> UserNoteLabelData = labelRepository.findById(userId);
			label = UserNoteLabelData.get();
			label.setLabelName(noteLabelValidation.getLabelName());
			label = labelRepository.save(label);
			return label;
		} else {
			logger.info("User not found");
			throw new CustomException(400, "User not found");
		}
	}

	@Override
	public void deleteLabel(Long labelId, String token) {

		Long userId = util.jwtTokenParser(token);
		Optional<UserDetails> userDetails = userDataRepository.findById(userId);
		Optional<UserNoteLabel> label = labelRepository.findById(labelId);

		if (userId != null && userDetails.isPresent() && userDetails.get().isVarified() && label.isPresent()) {

			labelRepository.deleteById(labelId);
		} else {
			logger.info("User not found");
			throw new CustomException(400, "User not found");
		}
	}

	@Override
	public Boolean isPinned(String token, Long noteId) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetails> userDetails = userDataRepository.findById(userId);
		UserNotes userNote = null;
		Optional<UserNotes> notes = userNoteRepository.findById(noteId);
		if (userDetails.isPresent() && notes.isPresent() && userDetails.get().isVarified()) {
			logger.info("User is present");
			userNote = notes.get();
			if (userNote.isPinned()) {
				logger.info("User note is Pinned ");
				userNote.setPinned(false);
				userNote.setNoteUpdateTime(LocalDateTime.now());
				userNoteRepository.save(userNote);
				logger.info("User note changed to unpinned ");
				return true;
			} else {
				userNote.setPinned(true);
				logger.info("User note is not Pinned ");
				userNote.setNoteUpdateTime(LocalDateTime.now());
				userNoteRepository.save(userNote);
				logger.info("User note is changed to  Pinned ");
				return false;
			}
		} else
			throw new CustomException(104, "Invalid token or user not verified");
	}

	@Override
	public Boolean isArchive(String token, Long noteId) {

		Long userId = util.jwtTokenParser(token);
		Optional<UserDetails> userDetails = userDataRepository.findById(userId);
		UserNotes userNote = null;
		Optional<UserNotes> notes = userNoteRepository.findById(noteId);
		if (userDetails.isPresent() && notes.isPresent() && userDetails.get().isVarified()) {
			logger.info("User is present");
			userNote = notes.get();
			if (userNote.isArchive()) {
				logger.info("User note is Archive ");
				userNote.setArchive(false);
				userNote.setNoteUpdateTime(LocalDateTime.now());
				userNoteRepository.save(userNote);
				logger.info("User note is removed from Archive ");
				return true;
			} else {
				userNote.setArchive(true);
				logger.info("User note is not Archive ");
				userNote.setNoteUpdateTime(LocalDateTime.now());
				userNoteRepository.save(userNote);
				logger.info("User note is Archive ");
				return false;
			}
		} else
			throw new CustomException(104, "Invalid token or user not verified");
	}

	@Override
	public Boolean isTrash(String token, Long noteId) {

		Long userId = util.jwtTokenParser(token);
		Optional<UserDetails> userDetails = userDataRepository.findById(userId);
		UserNotes userNote = null;
		Optional<UserNotes> notes = userNoteRepository.findById(noteId);
		if (userDetails.isPresent() && notes.isPresent() && userDetails.get().isVarified()) {
			logger.info("User note is present");
			userNote = notes.get();
			if (userNote.isTrace()) {
				logger.info("User note is in Trace ");
				userNote.setTrace(false);
				userNote.setNoteUpdateTime(LocalDateTime.now());
				userNoteRepository.save(userNote);
				logger.info("User note is removed from Trace ");
				return true;
			} else {
				userNote.setTrace(true);
				logger.info("User note is not in Trace ");
				userNote.setNoteUpdateTime(LocalDateTime.now());
				userNoteRepository.save(userNote);
				logger.info("User note is in Trace ");
				return false;
			}
		} else
			throw new CustomException(104, "Invalid token or user not verified");
	}

	@Override
	public UserNotes updateReminder(LocalDateTime reminderDate, String token, Long noteId) {

		Long userId = util.jwtTokenParser(token);
		Optional<UserDetails> userDetails = userDataRepository.findById(userId);
		UserNotes userNote = null;

		Optional<UserNotes> notes = userNoteRepository.findById(noteId);
		if (userDetails.isPresent() && notes.isPresent() && userDetails.get().isVarified()) {
			userNote = notes.get();
			userNote.setReminder(reminderDate);
			userNote.setNoteUpdateTime(LocalDateTime.now());
			userNoteRepository.save(userNote);
		} else
			throw new CustomException(104, "Invalid token or user not verified");
		return userNote;
	}

	@Override
	public List<UserNotes> showNoteList(String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetails> userDetails = userNoteRepository.findById(userId);//Error
		List<UserNotes> notes = null;
		if (userDetails.isPresent()) {
			if (userDetails.get().isVarified()) {
				notes = userNoteRepository.findAll();
			} else
				throw new CustomException(104, "Invalid token or user not verified");
		} else
			throw new CustomException(102, "User not exist");
		return notes;

	}

	@Override
	public List<UserDetails> showUserList(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserNoteLabel> showLabelList(String token) {
		Long userId = util.jwtTokenParser(token);
		List<UserNoteLabel> labels = null;
		if (userId != null) {
			Optional<UserDetails> userDetails = userDataRepository.findById(userId);
			if (userDetails.isPresent() && userDetails.get().isVarified()) {
				labels = labelRepository.findAll();
			}
			// throw exception
		}
		return labels;
	}

	@Override
	public List<UserDataValidation> showNoteColabratorList(UserDataValidation userDataValidation, String token) {
		// TODO Auto-generated method stub
		return null;
	}

}