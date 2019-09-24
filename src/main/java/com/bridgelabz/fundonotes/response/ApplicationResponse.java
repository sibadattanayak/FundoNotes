package com.bridgelabz.fundonotes.response;

public class ApplicationResponse {

	private Integer statusCode;
	private String description;

	public ApplicationResponse() {
	}

	public ApplicationResponse(Integer statusCode, String description) {

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
