package com.bridgelabz.fundonotes.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.bridgelabz.fundonotes.dto.ForgetPasswordDTO;
import com.bridgelabz.fundonotes.dto.ResetPasswordDTO;
import com.bridgelabz.fundonotes.dto.UserDataValidation;
import com.bridgelabz.fundonotes.dto.UserLoginValidation;
import com.bridgelabz.fundonotes.dto.UserNoteLabelValidation;
import com.bridgelabz.fundonotes.dto.UserNoteValidation;
import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNoteLabel;
import com.bridgelabz.fundonotes.model.UserNotes;
import com.bridgelabz.fundonotes.response.ApplicationResponse;
import com.bridgelabz.fundonotes.service.Label;
import com.bridgelabz.fundonotes.service.Note;
import com.bridgelabz.fundonotes.service.User;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*", exposedHeaders = { "jwtToken" })
@RequestMapping("/fundonotes")
public class UserController {

	@Autowired
	private User userService;
	@Autowired
	private Note noteService;
	@Autowired
	private Label labelService;
	/*
	 * @Autowired private ElasticSearchServiceImpl elasticService;
	 */
	private UserDetails userDetails;
	private String data = null;

	// ,consumes = { "application/json" }, produces = { "application/json" }
	@PostMapping(value = "/login")

	public ResponseEntity<ApplicationResponse> login(@RequestBody UserLoginValidation loginDto) {
		data = userService.userLogin(loginDto);
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@PostMapping(value = "/registration")

	public ResponseEntity<UserDetails> registration(@RequestBody UserDataValidation userDataValidation) {
		userDetails = userService.userRegistration(userDataValidation);
		return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
	}

	@PutMapping(value = "/forgotpassword")

	public ResponseEntity<ApplicationResponse> userForgotPassword(@RequestBody ForgetPasswordDTO forgetPasswordto) {
		data = userService.userForgotPassword(forgetPasswordto.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@PutMapping(value = "/resetpassword")

	public ResponseEntity<ApplicationResponse> userResetPassword(@RequestBody ResetPasswordDTO password,
			@RequestHeader String token) {
		data = userService.userResetPassword(password, token);
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@GetMapping(value = "/verifyuser/{token}")
	public void varifyUser(@PathVariable String token) {
		userService.userVarification(token);
	}

	@PostMapping(value = "/createnote")

	public ResponseEntity<List<UserNotes>> createNote(@RequestBody UserNoteValidation userNoteDto,
			@RequestHeader String token) {
		List<UserNotes> userNote = noteService.createNote(userNoteDto, token);
		return new ResponseEntity<List<UserNotes>>(userNote, HttpStatus.CREATED);
	}

	@PutMapping(value = "/updatenote")

	public ResponseEntity<UserNotes> updateNote(@RequestBody UserNotes userNotes, @RequestHeader String token) {
		System.out.println("Inside update Notes");
		UserNotes userNote = noteService.updateNote(userNotes, token);
		return new ResponseEntity<UserNotes>(userNote, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deletenote")

	public ResponseEntity<UserNotes> deleteNote(@RequestParam Long noteId, @RequestHeader String token) {
		UserNotes userNote = noteService.deleteNote(noteId, token);
		return new ResponseEntity<UserNotes>(userNote, HttpStatus.OK);
	}

	@PostMapping(value = "/createlabel")

	public ResponseEntity<List<UserNoteLabel>> createLabel(@RequestBody UserNoteLabelValidation labelDto,
			@RequestHeader String token) {
		List<UserNoteLabel> noteLabel = labelService.createLabel(labelDto, token);
		return new ResponseEntity<List<UserNoteLabel>>(noteLabel, HttpStatus.CREATED);
	}

	@PutMapping(value = "/updatelabel")

	public ResponseEntity<UserNoteLabel> updateLabel(@RequestBody UserNoteLabelValidation noteLabelValidation,
			@RequestHeader String token) {
		UserNoteLabel noteLabel = labelService.updateLabel(noteLabelValidation, token);
		return new ResponseEntity<UserNoteLabel>(noteLabel, HttpStatus.OK);
	}

	@DeleteMapping(value = "/deletelabel")

	public void deleteLabel(@RequestBody Long labelId, @RequestHeader String token) {
		labelService.deleteLabel(labelId, token);
	}

	@PutMapping(value = "/pinned")

	public ResponseEntity<Boolean> ifPinned(@RequestHeader String token, @RequestHeader Long noteId) {
		noteService.isPinned(token, noteId);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@PutMapping(value = "/archive")

	public ResponseEntity<Boolean> ifArchive(@RequestHeader String token, @RequestParam Long noteId) {
		noteService.isArchive(token, noteId);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@GetMapping(value = "/getarchivednotes")

	public ResponseEntity<List<UserNotes>> getArchivedNotes(@RequestHeader String token) {
		List<UserNotes> notes = noteService.getArchivedNotes(token);
		return new ResponseEntity<List<UserNotes>>(notes, HttpStatus.OK);
	}

	@PutMapping(value = "/trash")

	public ResponseEntity<Boolean> ifTrash(@RequestParam Long noteId, @RequestHeader String token) {
		noteService.isTrash(token, noteId);
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}

	@GetMapping(value = "/gettrash")

	public ResponseEntity<List<UserNotes>> getTrash(@RequestHeader String token) {
		List<UserNotes> notes = noteService.getTrash(token);
		return new ResponseEntity<List<UserNotes>>(notes, HttpStatus.OK);
	}

	@PutMapping(value = "/reminder")

	public ResponseEntity<UserNotes> updateReminder(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reminderDate,
			@RequestHeader String token, @RequestParam Long noteId) {
		UserNotes notes = noteService.updateReminder(reminderDate, token, noteId);
		return new ResponseEntity<UserNotes>(notes, HttpStatus.OK);
	}

	@GetMapping(value = "/getreminder")

	public ResponseEntity<List<UserNotes>> getReminders(@RequestHeader String token) {
		List<UserNotes> notes = noteService.getReminder(token);
		return new ResponseEntity<List<UserNotes>>(notes, HttpStatus.OK);
	}

	@GetMapping(value = "/showallusers")

	public ResponseEntity<List<UserDetails>> getAllUserList(@RequestHeader String token) {
		List<UserDetails> notes = userService.showUserList(token);
		return new ResponseEntity<List<UserDetails>>(notes, HttpStatus.OK);
	}

	@GetMapping(value = "/showallnotes")

	public ResponseEntity<List<UserNotes>> getAllNotes(@RequestHeader String token) {
		List<UserNotes> notes = noteService.showNoteList(token);
		return new ResponseEntity<List<UserNotes>>(notes, HttpStatus.OK);
	}

	@GetMapping(value = "/showalllabels")

	public ResponseEntity<List<UserNoteLabel>> getAllLabelList(@RequestHeader String token) {
		List<UserNoteLabel> labels = labelService.showLabelList(token);
		return new ResponseEntity<List<UserNoteLabel>>(labels, HttpStatus.OK);
	}

	@PostMapping(value = "/addcollaborator")

	public ResponseEntity<UserNoteLabel> addCollaborator(@RequestHeader String token, @RequestParam String email,
			@RequestParam Long noteId) {
		noteService.addCollaborater(token, email, noteId);
		return new ResponseEntity<UserNoteLabel>(HttpStatus.OK);
	}

	@DeleteMapping(value = "/removecollaborator")

	public ResponseEntity<UserNoteLabel> removeCollaborator(@RequestHeader String token, @RequestParam String email,
			@RequestParam Long noteId) {
		noteService.removeCollaborater(token, email, noteId);
		return new ResponseEntity<UserNoteLabel>(HttpStatus.OK);
	}

	@PutMapping(value = "/changecolor/{color}")

	public ResponseEntity<UserNotes> updateColor(@PathVariable("color") String color, @RequestParam Long noteId,
			@RequestHeader String token) {
		UserNotes notes = noteService.updateColor(color, token, noteId);
		return new ResponseEntity<UserNotes>(notes, HttpStatus.OK);
	}

	@GetMapping(value = "/showallcolabrators")

	public ResponseEntity<List<String>> colabratorList(@RequestParam Long noteId, @RequestHeader String token) {
		List<String> collaboratedUsers = noteService.getAllCollaborators(noteId, token);
		return new ResponseEntity<List<String>>(collaboratedUsers, HttpStatus.OK);
	}

	@GetMapping("/getNotesOnLabel")
	public List<UserNotes> getNotesOnLabel(@RequestParam Long labelId, @RequestHeader String token) {
		System.out.println(labelId + "     ");
		List<UserNotes> notes = noteService.getNotesOnLabel(labelId, token);
		return notes;
	}

	@PostMapping("/mappingNoteLabel")
	public ResponseEntity<ApplicationResponse> listLabel(@RequestParam Long noteId,
			@RequestBody UserNoteLabelValidation labeldto, @RequestHeader String token) {
		String status = noteService.listLabel(token, noteId, labeldto);
		ApplicationResponse response = new ApplicationResponse(HttpStatus.ACCEPTED.value(), status);
		return new ResponseEntity<ApplicationResponse>(response, HttpStatus.OK);
	}

	/*
	 * @GetMapping("/search_note") public List<UserNotes>
	 * searchNoteByData(@RequestParam String data) { return
	 * elasticService.searchNoteByData(data); }
	 */

}