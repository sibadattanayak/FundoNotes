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

import com.bridgelabz.fundonotes.dto.UserDataValidation;
import com.bridgelabz.fundonotes.dto.UserForgotPasswordValidation;
import com.bridgelabz.fundonotes.dto.UserLoginValidation;
import com.bridgelabz.fundonotes.dto.UserNoteValidation;
import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNotes;
import com.bridgelabz.fundonotes.service.Note;
import com.bridgelabz.fundonotes.service.User;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/fundonotes")
public class UserController {

	@Autowired
	private User userService;
	@Autowired
	private Note noteService;
	
	private UserNotes noteDetails;
	
	private UserDetails userDetails;
	
	String data = null;

	@GetMapping(value = "/login")
	public ResponseEntity<String> login(@RequestBody UserLoginValidation loginDto ) {
		data = userService.userLogin(loginDto);
		return new ResponseEntity<String>(data, HttpStatus.OK);
	}

	@PostMapping("/registration")
	public ResponseEntity<UserDetails> registration(@RequestBody UserDataValidation userDataValidation) {
		System.out.println("First registration api");
		userDetails = userService.userRegistration(userDataValidation);
		System.out.println("Inside registration api");
		return new ResponseEntity<UserDetails>(userDetails, HttpStatus.OK);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<String> userForgotPassword(@RequestHeader String email) {
		userService.userForgotPassword(email);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PutMapping("/resetpassword")
	public ResponseEntity<String> userResetPassword(@RequestBody UserForgotPasswordValidation password,
			@RequestHeader String token) {
		data = userService.userResetPassword(password, token);
		return new ResponseEntity<String>(data, HttpStatus.OK);
	}

	@GetMapping(value = "/verifyuser/{token}")
	public void varifyUser(@PathVariable String token) {
		userService.userVarification(token);
	}

	@PostMapping("/createnote")
	public ResponseEntity<UserNotes> createNote(@RequestBody String noteData, @RequestHeader String token) {
		noteDetails = noteService.createNote(noteData, token);
		return new ResponseEntity<UserNotes>(noteDetails, HttpStatus.OK);
	}

	@PutMapping("/updatenote")
	public ResponseEntity<UserNotes> updateNote(@RequestBody Long noteId, @RequestHeader String token) {
		noteDetails = noteService.updateNote(noteId, token);
		return new ResponseEntity<UserNotes>(noteDetails, HttpStatus.OK);

	}

	@DeleteMapping("/deletenote")
	public ResponseEntity<String> deleteNote(@RequestBody Long noteId, @RequestHeader String token) {
		data = noteService.deleteNote(noteId, token);
		return new ResponseEntity<String>(data, HttpStatus.OK);

	}

	@PostMapping("/createlabel")
	public void createLabel(@RequestBody String noteData, @RequestHeader String token) {
	}

	@PutMapping("/updatelabel/{labelid}")
	public void updateLabel(@RequestBody UserNoteValidation validNote, @RequestHeader String token) {
	}

	@DeleteMapping("/deletelabel/{labelid}")
	public void deleteLabel(@RequestBody UserNoteValidation validNote, @RequestHeader String token) {
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