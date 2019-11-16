package com.bridgelabz.fundonotes.response;

public class ApplicationResponse {

	private Integer statusCode;
	private String description;
	private Object data;

	public ApplicationResponse() {
	}

	public ApplicationResponse(Integer statusCode, Object data) {
		this.data = data;
		this.description = description;
	}

	public ApplicationResponse(Integer statusCode, String description, Object data) {
		this.data = data;
		this.statusCode = statusCode;
		this.description = description;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
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
