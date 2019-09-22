package com.bridgelabz.fundonotes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundonotes.response.ApplicationResponse;

@ControllerAdvice
public class ExceptionHandller {
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApplicationResponse> userException(CustomException e) {

		ApplicationResponse applicationResponse = new ApplicationResponse();
		applicationResponse.setDescription(e.getDescription());
		applicationResponse.setStatusCode(e.getStatusCode());
		return new ResponseEntity<ApplicationResponse>(applicationResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApplicationResponse> defaultException(Exception e) {

		ApplicationResponse applicationResponse = new ApplicationResponse();
		applicationResponse.setDescription(e.getMessage());
		applicationResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<ApplicationResponse>(applicationResponse, HttpStatus.BAD_REQUEST);
	}

}
 