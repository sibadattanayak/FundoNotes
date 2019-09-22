package com.bridgelabz.fundonotes.exception;

public class CustomException extends RuntimeException{

	private Integer statusCode;
	private String description;

	public CustomException(Integer statusCode, String description) {
		this.statusCode = statusCode;
		this.description = description;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
