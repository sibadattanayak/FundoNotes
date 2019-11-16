package com.bridgelabz.fundonotes.dto;

public class UserNoteLabelInfoDTO {

	private String labelName;

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public UserNoteLabelInfoDTO() {
		
	}
	
	public UserNoteLabelInfoDTO(String labelName) {
	
		this.labelName = labelName;
	}
	
}
