package com.bridgelabz.fundonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundonotes.dto.ForgotPasswordDTO;
import com.bridgelabz.fundonotes.dto.LoginDTO;
import com.bridgelabz.fundonotes.dto.NoteDTO;
import com.bridgelabz.fundonotes.dto.ValidateUser;
import com.bridgelabz.fundonotes.service.UserNoteService;
import com.bridgelabz.fundonotes.service.UserService;

@RestController
@RequestMapping("/fundonote")
public class UserController {

	@Autowired
	private UserService userServiceImpl;
	@Autowired
	private UserNoteService userNoteService;

	@GetMapping(value = "/login")
	public String login(@RequestBody LoginDTO loginDto) {
		return userServiceImpl.userLogin(loginDto);
	}

	@PostMapping("/registration")
	public String registration(@RequestBody ValidateUser validateUser) {
		return userServiceImpl.userRegistration(validateUser);
	}

	@GetMapping("/forgotpassword")
	public String userForgotPassword(@RequestHeader String email) {
		return userServiceImpl.userForgotPassword(email);
	}

	@PutMapping("/resetpassword/{password}")
	public String userResetPassword(@RequestBody ForgotPasswordDTO password, @RequestHeader String token) {
		return userServiceImpl.userResetPassword(password, token);
	}

	/*
	 * @PutMapping(value = "/verifyuser/{token}") public void
	 * varifyUser(@PathVariable String token) {
	 * userServiceImpl.updateRegistration(token); }
	 */
	@PostMapping("/createnote")
	public void createNote(@RequestBody String noteData, @RequestHeader String token) {
		userNoteService.createNote(noteData, token);
	}

	@PutMapping("/updatenote/{data}")
	public void updateNote(@RequestBody NoteDTO validNote) {
	}

	@DeleteMapping("/deletenote")
	public void deleteNote(@RequestBody NoteDTO validNote) {
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