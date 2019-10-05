package com.bridgelabz.fundonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundonotes.dto.ForgetPasswordDTO;
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
	private UserDetails userDetails;
	private String data = null;

	@PostMapping(value = "/login")
	public ResponseEntity<ApplicationResponse> login(@RequestBody UserLoginValidation loginDto) {
		data = userService.userLogin(loginDto);
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@PostMapping("/registration")
	public ResponseEntity<UserDetails> registration(@RequestBody UserDataValidation userDataValidation) {
		userDetails = userService.userRegistration(userDataValidation);
		return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
	}

	@PutMapping("/forgotpassword")
	public ResponseEntity<ApplicationResponse> userForgotPassword(@RequestBody ForgetPasswordDTO forgetPasswordto) {
		data = userService.userForgotPassword(forgetPasswordto.getEmail());
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@PutMapping("/resetpassword")
	public ResponseEntity<ApplicationResponse> userResetPassword(@RequestBody String password,
			@RequestHeader String token) {
		data = userService.userResetPassword(password, token);
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@GetMapping(value = "/verifyuser/{token}")
	public void varifyUser(@PathVariable String token) {
		userService.userVarification(token);
	}

	@PostMapping("/createnote")
	public ResponseEntity<UserNotes> createNote(@RequestBody UserNoteValidation userNoteDto,
			@RequestHeader String token) {
		UserNotes userNote = noteService.createNote(userNoteDto, token);
		return new ResponseEntity<UserNotes>(userNote, HttpStatus.CREATED);
	}

	@PutMapping("/updatenote")
	public ResponseEntity<UserNotes> updateNote(@RequestBody UserNoteValidation userNoteDto,
			@RequestHeader String token, @RequestHeader Long userNoteId) {
		UserNotes userNote = noteService.updateNote(userNoteDto, token, userNoteId);
		return new ResponseEntity<UserNotes>(userNote, HttpStatus.OK);
	}

	@DeleteMapping("/deletenote")
	public ResponseEntity<UserNotes> deleteNote(@RequestHeader Long noteId, @RequestHeader String token) {
		UserNotes userNote = noteService.deleteNote(noteId, token);
		return new ResponseEntity<UserNotes>(userNote, HttpStatus.OK);
	}

	@PostMapping("/createlabel")
	public ResponseEntity<UserNoteLabel> createLabel(@RequestBody String labelName, @RequestHeader String token) {
		UserNoteLabel noteLabel = labelService.createLabel(labelName, token);
		return new ResponseEntity<UserNoteLabel>(noteLabel, HttpStatus.OK);
	}

	@PutMapping("/updatelabel/{labelid}")
	public ResponseEntity<UserNoteLabel> updateLabel(@RequestBody UserNoteLabelValidation noteLabelValidation,
			@RequestHeader String token) {
		UserNoteLabel noteLabel = labelService.updateLabel(noteLabelValidation, token);
		return new ResponseEntity<UserNoteLabel>(noteLabel, HttpStatus.OK);
	}

	@DeleteMapping("/deletelabel/{labelid}")
	public void deleteLabel(@RequestBody Long labelId, @RequestHeader String token) {
		labelService.deleteLabel(labelId, token);
	}

	@GetMapping("/userlist")
	public void userList(@RequestBody String userNames) {
	}

	@GetMapping("/notelist")
	public void noteList(@RequestBody String userNoteList) {
	}

	@GetMapping("/labellist")
	public void labelList(@RequestBody String noteLabelList) {
	}

	@GetMapping("/colabratorlist")
	public void colabratorList(@RequestBody String noteColabratorList) {
	}

}