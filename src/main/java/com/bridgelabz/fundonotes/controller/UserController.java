package com.bridgelabz.fundonotes.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundonotes.dto.UserForgetPasswordDTO;
import com.bridgelabz.fundonotes.dto.UserResetPasswordDTO;
import com.bridgelabz.fundonotes.dto.UserInfoDTO;
import com.bridgelabz.fundonotes.dto.UserLoginDTO;
import com.bridgelabz.fundonotes.dto.UserNoteLabelInfoDTO;
import com.bridgelabz.fundonotes.dto.UserNoteInfoDTO;
import com.bridgelabz.fundonotes.model.UserDetailsModel;
import com.bridgelabz.fundonotes.model.LabelModel;
import com.bridgelabz.fundonotes.model.NoteModel;
import com.bridgelabz.fundonotes.response.ApplicationResponse;
import com.bridgelabz.fundonotes.service.ElasticSearchService;
import com.bridgelabz.fundonotes.service.Label;
import com.bridgelabz.fundonotes.service.Note;
import com.bridgelabz.fundonotes.service.User;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*", exposedHeaders = { "jwtToken" })
//@PropertySource("application.property")
@RequestMapping("/fundonotes")
public class UserController {

	@Autowired
	private User userService = null;
	@Autowired
	private Note noteService = null;
	@Autowired
	private Label labelService = null;

	@Autowired
	private ElasticSearchService elasticService = null;

	private UserDetailsModel userDetailsModel = null;
	private String data = null;

	// ,consumes = { "application/json" }, produces = { "application/json" }

	@PostMapping(value = "/login")
	public ResponseEntity<ApplicationResponse> login(@RequestBody UserLoginDTO loginDto) {
		data = userService.userLogin(loginDto);
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@PostMapping(value = "/registration")
	public ResponseEntity<ApplicationResponse> registration(@RequestBody UserInfoDTO userInfoDTO) {
		userDetailsModel = userService.userRegistration(userInfoDTO);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ApplicationResponse(HttpStatus.OK.value(), userDetailsModel));
	}

	@PutMapping(value = "/forgotpassword")
	public ResponseEntity<ApplicationResponse> userForgotPassword(@RequestBody UserForgetPasswordDTO forgetPasswordto) {
		data = userService.userForgotPassword(forgetPasswordto.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@PutMapping(value = "/resetpassword")
	public ResponseEntity<ApplicationResponse> userResetPassword(@RequestBody UserResetPasswordDTO password,
			@RequestHeader String token) {
		data = userService.userResetPassword(password, token);
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@GetMapping(value = "/verifyuser/{token}")
	public String varifyUser(@PathVariable String token) {
		return "User Verified Successfully >>> " + userService.userVarification(token);
	}

	@PostMapping(value = "/createnote")
	public ResponseEntity<List<NoteModel>> createNote(@RequestBody UserNoteInfoDTO userNoteDto,
			@RequestHeader String token) {
		List<NoteModel> userNote = noteService.createNote(userNoteDto, token);
		return new ResponseEntity<List<NoteModel>>(userNote, HttpStatus.CREATED);
	}

	@PutMapping(value = "/updatenote")
	public ResponseEntity<NoteModel> updateNote(@RequestBody NoteModel noteModel, @RequestHeader String token) {
		NoteModel userNote = noteService.updateNote(noteModel, token);
		return new ResponseEntity<NoteModel>(userNote, HttpStatus.OK);
	}

	@PutMapping("/updatenotereact")
	public ResponseEntity<String> updateNoteReact(@RequestParam Long id, @RequestBody UserNoteInfoDTO noteModel,
			@RequestHeader String token) {
		String result = noteService.updateNoteReact(id, noteModel, token);
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deletenote")
	public ResponseEntity<NoteModel> deleteNote(@RequestParam Long noteId, @RequestHeader String token) {
		NoteModel userNote = noteService.deleteNote(noteId, token);
		return new ResponseEntity<NoteModel>(userNote, HttpStatus.OK);
	}

	@GetMapping(value = "/showallnotes")
	public ResponseEntity<List<NoteModel>> getAllNotes(@RequestHeader String token) {
		List<NoteModel> notes = noteService.showNoteList(token);
		return new ResponseEntity<List<NoteModel>>(notes, HttpStatus.OK);
	}

	@PostMapping(value = "/createlabel")
	public ResponseEntity<List<LabelModel>> createLabel(@RequestBody UserNoteLabelInfoDTO labelDto,
			@RequestHeader String token) {
		List<LabelModel> noteLabel = labelService.createLabel(labelDto, token);
		return new ResponseEntity<List<LabelModel>>(noteLabel, HttpStatus.CREATED);
	}

//	@PutMapping(value = "/updatelabel")
//	public ResponseEntity<LabelModel> updateLabel(@RequestParam UserNoteLabelInfoDTO noteLabelValidation,
//			@RequestHeader String token) {
//		LabelModel noteLabel = labelService.updateLabel(noteLabelValidation, token);
//		return new ResponseEntity<LabelModel>(noteLabel, HttpStatus.OK);
//	}

	@PutMapping("/updatelabel")
	public ResponseEntity<LabelModel> updateLabel(@RequestParam Long labelId,
			@RequestBody UserNoteLabelInfoDTO labelDto, @RequestHeader String token) {
		LabelModel label = labelService.updateLabel(labelDto, labelId, token);
		return new ResponseEntity<LabelModel>(label, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deletelabel")
	public void deleteLabel(@RequestParam Long labelId, @RequestHeader String token) {
		labelService.deleteLabel(labelId, token);
	}

	@GetMapping(value = "/showalllabels")
	public ResponseEntity<List<LabelModel>> getAllLabelList(@RequestHeader String token) {
		List<LabelModel> labels = labelService.showLabelList(token);
		return new ResponseEntity<List<LabelModel>>(labels, HttpStatus.OK);
	}

	@GetMapping("/getnotesonlabel")
	public List<NoteModel> getNotesOnLabel(@RequestParam Long labelId, @RequestHeader String token) {
		List<NoteModel> notes = noteService.getNotesOnLabel(labelId, token);
		return notes;
	}

	@PostMapping("/mappingnotelabel")
	public ResponseEntity<ApplicationResponse> listLabel(@RequestParam Long noteId,
			@RequestBody UserNoteLabelInfoDTO labeldto, @RequestHeader String token) {
		String status = noteService.listLabel(token, noteId, labeldto);
		ApplicationResponse response = new ApplicationResponse(HttpStatus.ACCEPTED.value(), status);
		return new ResponseEntity<ApplicationResponse>(response, HttpStatus.OK);
	}

	@PutMapping(value = "/pinned")
	public ResponseEntity<Boolean> ifPinned(@RequestHeader String token, @RequestParam Long noteId) {
		noteService.isPinned(token, noteId);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@PutMapping(value = "/archive")
	public ResponseEntity<Boolean> ifArchive(@RequestHeader String token, @RequestParam Long noteId) {
		noteService.isArchive(token, noteId);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@GetMapping(value = "/getarchivednotes")
	public ResponseEntity<List<NoteModel>> getArchivedNotes(@RequestHeader String token) {
		List<NoteModel> notes = noteService.getArchivedNotes(token);
		return new ResponseEntity<List<NoteModel>>(notes, HttpStatus.OK);
	}

	@PutMapping(value = "/trash")
	public ResponseEntity<Boolean> ifTrash(@RequestParam Long noteId, @RequestHeader String token) {
		noteService.isTrash(token, noteId);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@GetMapping(value = "/gettrash")
	public ResponseEntity<List<NoteModel>> getTrash(@RequestHeader String token) {
		List<NoteModel> notes = noteService.getTrash(token);
		return new ResponseEntity<List<NoteModel>>(notes, HttpStatus.OK);
	}

	@PutMapping(value = "/reminder")
	public ResponseEntity<NoteModel> updateReminder(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminderDate,
			@RequestHeader String token, @RequestParam Long noteId) {
		NoteModel notes = noteService.updateReminder(reminderDate, token, noteId);
		return new ResponseEntity<NoteModel>(notes, HttpStatus.OK);
	}

	@GetMapping(value = "/getreminder")
	public ResponseEntity<List<NoteModel>> getReminders(@RequestHeader String token) {
		List<NoteModel> notes = noteService.getReminder(token);
		return new ResponseEntity<List<NoteModel>>(notes, HttpStatus.OK);
	}

	@GetMapping(value = "/showallusers")
	public ResponseEntity<List<UserDetailsModel>> getAllUserList(@RequestHeader String token) {
		List<UserDetailsModel> notes = userService.showUserList(token);
		return new ResponseEntity<List<UserDetailsModel>>(notes, HttpStatus.OK);
	}

	@PutMapping(value = "/changecolor/{color}")
	public ResponseEntity<NoteModel> updateColor(@PathVariable("color") String color, @RequestParam Long noteId,
			@RequestHeader String token) {
		NoteModel notes = noteService.updateColor(color, token, noteId);
		return new ResponseEntity<NoteModel>(notes, HttpStatus.OK);
	}

	@PostMapping(value = "/addcollaborator")
	public ResponseEntity<LabelModel> addCollaborator(@RequestHeader String token, @RequestParam String email,
			@RequestParam Long noteId) {
		noteService.addCollaborater(token, email, noteId);
		return new ResponseEntity<LabelModel>(HttpStatus.OK);
	}

	@DeleteMapping(value = "/removecollaborator")
	public ResponseEntity<LabelModel> removeCollaborator(@RequestHeader String token, @RequestParam String email,
			@RequestParam Long noteId) {
		noteService.removeCollaborater(token, email, noteId);
		return new ResponseEntity<LabelModel>(HttpStatus.OK);
	}

	@GetMapping(value = "/showallcolabrators")
	public ResponseEntity<List<String>> colabratorList(@RequestParam Long noteId, @RequestHeader String token) {
		List<String> collaboratedUsers = noteService.getAllCollaborators(noteId, token);
		return new ResponseEntity<List<String>>(collaboratedUsers, HttpStatus.OK);
	}

	@GetMapping("/searchnote")
	public List<NoteModel> searchNoteByData(@RequestParam String data) {
		return elasticService.searchNoteByData(data);
	}

}