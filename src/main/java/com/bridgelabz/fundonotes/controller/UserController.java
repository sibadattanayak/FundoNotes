package com.bridgelabz.fundonotes.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.bridgelabz.fundonotes.serviceimpl.UserServiceImpl;

@RestController
public class UserController {

	@Autowired
	private UserServiceImpl userServiceImpl;

	@GetMapping(value = { "/", "/login" })
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		userServiceImpl = new UserServiceImpl();
		modelAndView.addObject("user", userServiceImpl);
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid UserServiceImpl user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		UserServiceImpl userExists = userServiceImpl.findUserByEmail(userServiceImpl.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userServiceImpl.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new UserServiceImpl());
			modelAndView.setViewName("registration");

		}
		return modelAndView;
	}
}
