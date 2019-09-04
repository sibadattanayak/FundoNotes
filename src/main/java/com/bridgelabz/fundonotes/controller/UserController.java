package com.bridgelabz.fundonotes.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bridgelabz.fundonotes.dto.ValidateUser;
import com.bridgelabz.fundonotes.serviceimpl.UserServiceImpl;

@RestController
@RequestMapping("/fundonote")
public class UserController {

	@Autowired
	private ValidateUser validateUser;
	@Autowired
	private UserServiceImpl userServiceImpl;

	@GetMapping(value = "/login")
	public List<ValidateUser> login() {
		return userServiceImpl.userLogin(validateUser);
	}

	@PostMapping(value = "/registration")
	public void registration(@RequestBody ValidateUser validateUser) {
	 userServiceImpl.userRegistration(validateUser);
	}

	/*
	 * @RequestMapping(value = "/registration", method = RequestMethod.POST) public
	 * ModelAndView createNewUser(@Valid UserServiceImpl user, BindingResult
	 * bindingResult) { ModelAndView modelAndView = new ModelAndView();
	 * UserServiceImpl userExists =
	 * userServiceImpl.findUserByEmail(userServiceImpl.getEmail()); if (userExists
	 * != null) { bindingResult.rejectValue("email", "error.user",
	 * "There is already a user registered with the email provided"); } if
	 * (bindingResult.hasErrors()) { modelAndView.setViewName("registration"); }
	 * else { userServiceImpl.saveUser(user);
	 * modelAndView.addObject("successMessage",
	 * "User has been registered successfully"); modelAndView.addObject("user", new
	 * UserServiceImpl()); modelAndView.setViewName("registration");
	 * 
	 * } return modelAndView; }
	 */
}
