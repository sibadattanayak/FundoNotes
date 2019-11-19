package com.bridgelabz.fundonotes.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.fundonotes.configuration.RabitMqConfig;
import com.bridgelabz.fundonotes.dto.UserInfoDTO;
import com.bridgelabz.fundonotes.dto.UserLoginDTO;
import com.bridgelabz.fundonotes.dto.UserNoteInfoDTO;
import com.bridgelabz.fundonotes.dto.UserNoteLabelInfoDTO;
import com.bridgelabz.fundonotes.dto.UserResetPasswordDTO;
import com.bridgelabz.fundonotes.exception.CustomException;
import com.bridgelabz.fundonotes.model.LabelModel;
import com.bridgelabz.fundonotes.model.NoteModel;
import com.bridgelabz.fundonotes.model.UserDetailsModel;
import com.bridgelabz.fundonotes.repository.UserDataRepository;
import com.bridgelabz.fundonotes.repository.UserNoteLabelRepository;
import com.bridgelabz.fundonotes.repository.UserNoteRepository;
import com.bridgelabz.fundonotes.response.RabbitMessage;
import com.bridgelabz.fundonotes.service.Label;
import com.bridgelabz.fundonotes.service.Note;
import com.bridgelabz.fundonotes.service.User;
import com.bridgelabz.fundonotes.util.Utility;

@Service
public class UserServiceImplementation implements Label, User, Note {

	@Autowired
	private UserDataRepository userDataRepository = null;
	@Autowired
	private ModelMapper modelMapper = null;
	@Autowired
	private Utility util = null;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder = null;
	@Autowired
	private UserNoteRepository userNoteRepository = null;
	@Autowired
	private RabbitTemplate rabbitTemplate = null;
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate = null;

	@Autowired
	private UserNoteLabelRepository labelRepository = null;

	@Autowired
	private ElasticSearchServiceImpl elasticSearchService = null;

	private String message = null;

	private static Logger logger = Logger.getLogger(UserServiceImplementation.class);

	static {
		PropertyConfigurator
				.configure("/home/admin1/Desktop/SpringWorkspace/FundoNotes/src/main/resources/log4j.properties");
	}

	@Override
	@Transactional
	public String addCollaborater(String token, String email, Long noteId) {
		Long userId = util.jwtTokenParser(token);

		if (userId.equals(null)) {
			logger.error("User not found with id>> "+userId);
			throw new CustomException(400, "User not found");
		}
		Optional<UserDetailsModel> user = userDataRepository.findById(userId);
		Optional<UserDetailsModel> checkUser = userDataRepository.findByEmail(email);

		if (!checkUser.isPresent()) {
			throw new CustomException(400, "Collaborating email not found");
		}
		Optional<NoteModel> checkNote = user.get().getNotesList().stream().filter(data -> data.getId() == noteId)
				.findFirst();
		if (!checkNote.isPresent()) {
			throw new CustomException(400, "Note not found ");
		}
		Optional<NoteModel> collaboratedNote = checkUser.get().getCollabratorNoteList().stream()
				.filter(data -> data.getId() == noteId).findFirst();
		if (collaboratedNote.isPresent()) {
			throw new CustomException(400, "User already collaborated");
		}
		user.get().getCollabratorNoteList().add(checkNote.get());
		checkUser.get().getCollabratorNoteList().add(checkNote.get());
		checkNote.get().getCollabratorUserList().add(checkUser.get());
		userNoteRepository.save(checkNote.get());
		userDataRepository.save(user.get());
		userDataRepository.save(checkUser.get());
		return "User Collaborated";
	}

	@Override
	public String removeCollaborater(String token, String email, Long noteId) {

		Long userId = util.jwtTokenParser(token);

		if (userId.equals(null)) {
			throw new CustomException(400, "User not found");
		}
		Optional<UserDetailsModel> user = userDataRepository.findById(userId);
		Optional<UserDetailsModel> checkUser = userDataRepository.findByEmail(email);

		if (!checkUser.isPresent()) {
			throw new CustomException(400, "collaborated user not found ");
		}
		Optional<NoteModel> notePresent = userNoteRepository.findById(noteId);

		Optional<NoteModel> collaboratedNote = checkUser.get().getCollabratorNoteList().stream()
				.filter(data -> data.getId() == noteId).findFirst();
		if (!collaboratedNote.isPresent()) {
			throw new CustomException(400, "collaborated note  not found ");
		}
		notePresent.get().getCollabratorUserList().remove(checkUser.get());
		checkUser.get().getCollabratorNoteList().remove(collaboratedNote.get());
		userNoteRepository.save(notePresent.get());
		userDataRepository.save(user.get());
		return "collaborated user removed";
	}

	@Override
	@Transactional
	public UserDetailsModel userRegistration(UserInfoDTO userInfoDTO) {
		try {
			Optional<UserDetailsModel> findMail = userDataRepository.findByEmail(userInfoDTO.getEmail());
			if (findMail.isPresent())
				throw new CustomException(302, "User Already Registered with this mail");
			UserDetailsModel userDetailsModel = modelMapper.map(userInfoDTO, UserDetailsModel.class);

			userDetailsModel.setPassword(bCryptPasswordEncoder.encode(userDetailsModel.getPassword()));
			userDetailsModel.setCreateTime(LocalDateTime.now());
			userDetailsModel.setUpdateTime(LocalDateTime.now());
			userDetailsModel.setVarified(true);

			UserDetailsModel userDetails1 = userDataRepository.save(userDetailsModel);
			logger.info("User Registered Successfully");
			String token = null;
			if (userDetails1 != null) {
				token = util.jwtTokenGenerator(userDetails1.getUserId());
				String url = "http://localhost:8081/fundonotes/verifyuser/";

				RabbitMessage msg = new RabbitMessage();
				msg.setEmail(userInfoDTO.getEmail());
				msg.setLink(url);
				msg.setToken(token);
				rabbitTemplate.convertAndSend(RabitMqConfig.EXCHANGE_NAME, RabitMqConfig.ROUTING_KEY, msg);
				logger.info("varification mail is sent");
				return userDetails1;

			}
		} catch (Exception e) {
			throw new InternalError("", e);
		}
		return null;
	}

	@Override
	public String userVarification(String token) {
		String message = null;
		Long userId = util.jwtTokenParser(token);
		if (userId != null) {
			UserDetailsModel details = userDataRepository.getOne(userId);
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
	public String userLogin(UserLoginDTO loginDto) {
		String token = null;
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findByEmail(loginDto.getUserEmail());

		if (!userDetailsModel.isPresent()) {
			logger.info("Login attempted with unregistered email --> " + loginDto.getUserEmail());
			throw new CustomException(404, "email does not exist");
		}
		if (userDetailsModel.isPresent() && userDetailsModel.get().isVarified()
				&& bCryptPasswordEncoder.matches(loginDto.getUserPassword(), userDetailsModel.get().getPassword())) {
			token = util.jwtTokenGenerator(userDetailsModel.get().getUserId());
			System.out.println("Redis >> " + redisTemplate.opsForValue().get(token));
			redisTemplate.opsForValue().set(token, loginDto.getUserEmail());
			logger.info(loginDto.getUserEmail() + " user logged in successfully");
		} else {
			logger.info("Login attempted --> " + loginDto.getUserEmail());
			throw new CustomException(404, "user not verified or invalid user");
		}
		logger.info("Login attempted with email - " + loginDto.getUserEmail());
		return token;
	}

	@Override

	public String userForgotPassword(String email) {
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findByEmail(email);
		if (userDetailsModel.isPresent()) {
			String token = util.jwtTokenGenerator(userDetailsModel.get().getUserId());
			util.javaMail(userDetailsModel.get().getEmail(), token, "http://localhost:3000/resetpassword/");
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
	public String userResetPassword(UserResetPasswordDTO password, String token) {

		Long userId = util.jwtTokenParser(token);
		if (userId != null) {
			UserDetailsModel details = userDataRepository.getOne(userId);
			if (details.isVarified() == true) {
				details.setUpdateTime(LocalDateTime.now());
				details.setPassword(bCryptPasswordEncoder.encode(password.getPassword()));
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

	public List<NoteModel> createNote(UserNoteInfoDTO userNoteDto, String token) {
		Long userId = util.jwtTokenParser(token);
		NoteModel notes = null;
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		if (userId != null && userDetailsModel.isPresent() && userDetailsModel.get().isVarified()) {
			notes = modelMapper.map(userNoteDto, NoteModel.class);
			notes.setNoteCreateTime(LocalDateTime.now());
			notes.setNoteUpdateTime(LocalDateTime.now());
			userDetailsModel.get().getNotesList().add(notes);
			userDataRepository.save(userDetailsModel.get());
			logger.info("Note created successfully");
			elasticSearchService.createNote(notes);
			return userDetailsModel.get().getNotesList();
		} else
			logger.info("Note couldn't be created");
		throw new CustomException(106, "Note couldn't be created");
	}

	@Override
	public NoteModel updateNote(NoteModel userNote, String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		logger.info("UserId for creating notes >> " + userId);
		Optional<NoteModel> notes = null;
		if (userDetailsModel.isPresent() && userId != null) {
			notes = userNoteRepository.findById(userNote.getId());
			if (notes.isPresent() && userDetailsModel.get().isVarified()) {
				if (userNote.getNoteTitle() != null) {
					notes.get().setNoteTitle(userNote.getNoteTitle());

				} else {
					logger.info("User notes data not found");
					throw new CustomException(104, "Invalid User or User Not verified");
				}
				if (userNote.getNoteDescription() != null) {
					notes.get().setNoteDescription(userNote.getNoteDescription());
				} else {
					logger.info("User note not found for updating note");
					throw new CustomException(104, "Invalid User or NoteDescription is null");
				}
				notes.get().setNoteUpdateTime(LocalDateTime.now());

				userNoteRepository.save(notes.get());
				// elasticSearchService.updateNote(notes.get());
			} else {
				throw new CustomException(104, "Invalid User or Token");
			}
		}
		return notes.get();
	}

	@Override
	public String updateNoteReact(Long id, UserNoteInfoDTO noteModel, String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetails = userDataRepository.findById(userId);
		Optional<NoteModel> note = null;
		if (userDetails.isPresent()) {
			note = userNoteRepository.findById(id);
			if (note.isPresent() && userDetails.get().isVarified()) {
				note.get().setNoteTitle(noteModel.getNoteTitle());
				note.get().setNoteDescription(noteModel.getNoteDescription());
				note.get().setNoteUpdateTime(LocalDateTime.now());
				userNoteRepository.save(note.get());
				elasticSearchService.updateNote(note.get());
			} else
				throw new CustomException(104, "Invalid token or user not verified");
		} else
			throw new CustomException(102, "User not exist");

		return "update note";

	}

	@Override
	public NoteModel deleteNote(Long noteId, String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		Optional<NoteModel> notes = null;

		if (userDetailsModel.isPresent()) {
			notes = userNoteRepository.findById(noteId);
			if (notes.isPresent() && userDetailsModel.get().isVarified()) {
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

	@Override
	public List<LabelModel> createLabel(UserNoteLabelInfoDTO labelDto, String token) {

		Long userId = util.jwtTokenParser(token);
		LabelModel label = null;
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);

		if (userId != null && userDetailsModel.isPresent() && userDetailsModel.get().isVarified()) {
			logger.info("User is present and verified");
			label = modelMapper.map(labelDto, LabelModel.class);
			logger.info("DTO mapped with Model");
			userDetailsModel.get().getLabelList().add(label);
			userDataRepository.save(userDetailsModel.get());
			logger.info("Label created successfully");
			return userDetailsModel.get().getLabelList();
		} else {
			logger.info("User Details not found of user not found");
			throw new CustomException(400, "User Details not found of user not found");
		}
	}

	@Override
	public LabelModel updateLabel(UserNoteLabelInfoDTO noteLabelValidation, String token) {

		Long userId = util.jwtTokenParser(token);
		LabelModel label = null;
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);

		if (userId != null && userDetailsModel.isPresent() && userDetailsModel.get().isVarified()) {
			Optional<LabelModel> UserNoteLabelData = labelRepository.findById(userId);
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
	public LabelModel updateLabel(UserNoteLabelInfoDTO labelDto, Long labelId, String token) {
		Long userId = util.jwtTokenParser(token);
		LabelModel labelData = null;
		if (userId != null) {
			Optional<UserDetailsModel> userDetails = userDataRepository.findById(userId);
			if (userDetails.isPresent() && userDetails.get().isVarified()) {
				Optional<LabelModel> label = labelRepository.findById(labelId);
				if (label.isPresent()) {
					labelData = label.get();
					labelData.setLabelName(labelDto.getLabelName());
					labelData = labelRepository.save(labelData);
				}
				// throw exception
			}
			// throw exception
		}
		return labelData;
	}

	@Override
	public void deleteLabel(Long labelId, String token) {

		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		Optional<LabelModel> label = labelRepository.findById(labelId);

		if (userId != null && userDetailsModel.isPresent() && userDetailsModel.get().isVarified()
				&& label.isPresent()) {

			labelRepository.deleteById(labelId);
		} else {
			logger.info("User not found");
			throw new CustomException(400, "User not found");
		}
	}

	@Override
	public Boolean isPinned(String token, Long noteId) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		NoteModel userNote = null;
		Optional<NoteModel> notes = userNoteRepository.findById(noteId);
		if (userDetailsModel.isPresent() && notes.isPresent() && userDetailsModel.get().isVarified()) {
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
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		NoteModel userNote = null;
		Optional<NoteModel> notes = userNoteRepository.findById(noteId);
		if (userDetailsModel.isPresent() && notes.isPresent() && userDetailsModel.get().isVarified()) {
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
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		NoteModel userNote = null;
		Optional<NoteModel> notes = userNoteRepository.findById(noteId);
		if (userDetailsModel.isPresent() && notes.isPresent() && userDetailsModel.get().isVarified()) {
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
	public NoteModel updateReminder(LocalDateTime reminderDate, String token, Long noteId) {

		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		NoteModel userNote = null;

		Optional<NoteModel> notes = userNoteRepository.findById(noteId);
		if (userDetailsModel.isPresent() && notes.isPresent() && userDetailsModel.get().isVarified()) {
			userNote = notes.get();
			userNote.setReminder(reminderDate);
			userNote.setNoteUpdateTime(LocalDateTime.now());
			userNoteRepository.save(userNote);
		} else
			throw new CustomException(104, "Invalid token or user not verified");
		return userNote;
	}

	@Override
	public List<NoteModel> showNoteList(String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		List<NoteModel> notes = null;
		if (userDetailsModel.isPresent()) {
			if (userDetailsModel.get().isVarified()) {
				notes = userDetailsModel.get().getNotes().stream().filter(data -> !data.isArchive() && !data.isTrace())
						.collect(Collectors.toList());
			} else
				throw new CustomException(104, "Invalid token or user not verified");
		} else
			throw new CustomException(102, "User not exist");
		return notes;

	}

	@Override
	public List<UserDetailsModel> showUserList(String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		List<UserDetailsModel> userData = null;
		if (userDetailsModel.isPresent()) {
			userData = userDataRepository.findAll();
			return userData;
		} else {
			throw new CustomException(102, "User not exist");
		}
	}

	@Override
	public List<LabelModel> showLabelList(String token) {
		Long userId = util.jwtTokenParser(token);
		List<LabelModel> labels = null;
		logger.info("Inside User's lable list ");
		if (userId != null) {
			Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
			if (userDetailsModel.isPresent() && userDetailsModel.get().isVarified()) {
				labels = userDetailsModel.get().getLabelList();
				logger.info("User lables found ");
				return labels;
			} else {
				logger.info("User lables not found ");
				throw new CustomException(400, "User not verified");
			}
		} else {
			logger.info("User lables not found ");
			throw new CustomException(400, "User not exist");
		}

	}

	private List<NoteModel> getCollaboratedNoteForUser(Long userId) {
		List<NoteModel> collaborateNotes = new ArrayList<NoteModel>();
		List<Long> collaboratednoteId = userNoteRepository.findBycollabratorUserList(userId);
		if (collaboratednoteId == null) {
			throw new CustomException(404, "list is empty");
		}
		for (Long noteid : collaboratednoteId) {
			Optional<NoteModel> collaboratedNote = userNoteRepository.findById(noteid);
			collaborateNotes.add(collaboratedNote.get());
		}

		return collaborateNotes;
	}

	@Override
	public List<String> getAllCollaborators(Long noteId, String token) {

		Long userId = util.jwtTokenParser(token);
		ArrayList<String> collabUserList = new ArrayList<String>();
		Optional<UserDetailsModel> userModel = userDataRepository.findById(userId);
		if (!userModel.isPresent()) {
			throw new CustomException(400, "User not found 1");
		}
		List<Long> collaUserIds = userNoteRepository.findBycollabratorNoteId(noteId);
		for (Long eachUserId : collaUserIds) {
			Optional<UserDetailsModel> user = userDataRepository.findById(eachUserId);
			collabUserList.add(user.get().getEmail());
		}
		return collabUserList;

	}

	@Override
	public NoteModel updateColor(String color, String token, Long noteId) {

		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		NoteModel userNote = null;

		if (userDetailsModel.isPresent()) {
			Optional<NoteModel> notes = userNoteRepository.findById(noteId);
			if (notes.isPresent() && userDetailsModel.get().isVarified()) {
				userNote = notes.get();
				userNote.setColor(color);
				userNote.setNoteUpdateTime(LocalDateTime.now());
				userNoteRepository.save(userNote);
			} else
				throw new CustomException(104, "Invalid token or user not verified");
		} else
			throw new CustomException(102, "User not exist");
		return userNote;
	}

	@Override
	public List<NoteModel> getReminder(String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);

		List<NoteModel> notesList = null;
		if (userDetailsModel.isPresent()) {
			if (userDetailsModel.get().isVarified()) {
				notesList = userDetailsModel.get().getNotes();
				notesList = notesList.stream().filter(notecheck -> notecheck.getReminder() != null)
						.collect(Collectors.toList());
			} else
				throw new CustomException(104, "Invalid token or user not verified");
		} else
			throw new CustomException(102, "User not exist");
		return notesList;
	}

	@Override
	public List<NoteModel> getTrash(String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		List<NoteModel> notesList = null;
		if (userDetailsModel.isPresent()) {
			if (userDetailsModel.get().isVarified()) {
				notesList = userDetailsModel.get().getNotes();
				notesList = notesList.stream().filter(notecheck -> notecheck.isTrace()).collect(Collectors.toList());
			} else
				throw new CustomException(104, "Invalid token or user not verified");
		} else
			throw new CustomException(102, "User not exist");
		return notesList;
	}

	@Override
	public List<NoteModel> getArchivedNotes(String token) {
		Long userId = util.jwtTokenParser(token);
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		List<NoteModel> notesList = null;
		if (userDetailsModel.isPresent()) {
			if (userDetailsModel.get().isVarified()) {
				notesList = userDetailsModel.get().getNotes();
				notesList = notesList.stream().filter(notecheck -> notecheck.isArchive()).collect(Collectors.toList());
			} else
				throw new CustomException(104, "Invalid token or user not verified");
		} else
			throw new CustomException(102, "User not exist");
		return notesList;
	}

	@Override
	public List<NoteModel> getNotesOnLabel(Long labelId, String token) {
		Long userId = util.jwtTokenParser(token);
		List<NoteModel> notes = null;
		Optional<UserDetailsModel> userDetailsModel = userDataRepository.findById(userId);
		Optional<LabelModel> labelModel = null;
		if (userDetailsModel.isPresent() && userDetailsModel.get().isVarified()) {
			labelModel = labelRepository.findById(labelId);
			if (labelModel.isPresent()) {
				notes = getMultipleNotesOfLabel(labelId);
			}
		}
		return notes;
	}

	private List<NoteModel> getMultipleNotesOfLabel(Long labelId) {
		ArrayList<NoteModel> noteData = new ArrayList<NoteModel>();
		List<Long> noteByLabel = labelRepository.findByLabelId(labelId);
		if (!noteByLabel.isEmpty()) {
			for (Long noteId : noteByLabel) {
				Optional<NoteModel> noteById = userNoteRepository.findById(noteId);
				noteData.add(noteById.get());
			}
		}
		return noteData;
	}

	@Override
	public String listLabel(String token, Long noteId, UserNoteLabelInfoDTO labelDto) {
		Long verifiedUser = util.jwtTokenParser(token);

		Optional<UserDetailsModel> userModel = userDataRepository.findById(verifiedUser);

		if (userModel.isPresent()) {
			Optional<LabelModel> isLabelPresent = userModel.get().getLabelList().stream()
					.filter(data -> data.getLabelName().equalsIgnoreCase(labelDto.getLabelName())).findFirst();
			if (isLabelPresent.isPresent()) {
				Optional<NoteModel> noteModel = userNoteRepository.findById(noteId);
				if (noteModel.isPresent()) {
					Optional<LabelModel> isnotelabelPresent = noteModel.get().getLabel().stream()
							.filter(data -> data.getLabelName().equalsIgnoreCase(labelDto.getLabelName())).findFirst();
					if (isnotelabelPresent.isPresent()) {
						throw new CustomException(HttpStatus.BAD_REQUEST.value(), "label already exist");
					}
					noteModel.get().getLabel().add(isLabelPresent.get());
					userNoteRepository.save(noteModel.get());
					return "label added to note";
				}
			}
		}
		return "user not present";
	}
}