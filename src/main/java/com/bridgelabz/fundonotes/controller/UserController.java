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
import com.bridgelabz.fundonotes.dto.UserNoteValidation;
import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.response.ApplicationResponse;
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

	private UserDetails userDetails;
	String data = null;

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
	public ResponseEntity<ApplicationResponse> createNote(@RequestBody String noteData, @RequestHeader String token) {
		noteService.createNote(noteData, token);
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@PutMapping("/updatenote")
	public ResponseEntity<ApplicationResponse> updateNote(@RequestBody Long noteId, @RequestHeader String token) {
		noteService.updateNote(noteId, token);
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));
	}

	@DeleteMapping("/deletenote")
	public ResponseEntity<ApplicationResponse> deleteNote(@RequestBody Long noteId, @RequestHeader String token) {
		data = noteService.deleteNote(noteId, token);
		return ResponseEntity.status(HttpStatus.OK).body(new ApplicationResponse(HttpStatus.OK.value(), data));

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