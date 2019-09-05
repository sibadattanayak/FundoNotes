package com.bridgelabz.fundonotes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundonotes.dto.LoginDTO;
import com.bridgelabz.fundonotes.dto.ValidateUser;
import com.bridgelabz.fundonotes.serviceimpl.UserServiceImpl;

@RestController
@RequestMapping("/fundonote")
public class UserController {

	
	@Autowired
	private UserServiceImpl userServiceImpl;

	@GetMapping(value = "/login")
	public List<ValidateUser> login(@RequestBody LoginDTO loginDto) {
		return userServiceImpl.userLogin(loginDto);
	}

	@PostMapping("/registration")
	public String registration(@RequestBody ValidateUser validateUser) {
		String token = userServiceImpl.userRegistration(validateUser);
		return token;
	}

	@GetMapping(value = "/verifyuser/{token}")
	public void varifyUser(@PathVariable String token) {
		userServiceImpl.updateRegistration(token);
	}
}
