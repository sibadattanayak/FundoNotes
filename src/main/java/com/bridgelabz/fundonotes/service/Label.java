package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.UserNoteLabelValidation;

public interface Label {

	void createLabel(UserNoteLabelValidation noteLabelValidation, String token);

	void updateLabel(UserNoteLabelValidation noteLabelValidation, String token);

	void deleteLabel(UserNoteLabelValidation noteLabelValidation, String token);

	List<UserNoteLabelValidation> showNoteLabelList(UserNoteLabelValidation validateNoteLabel);

}
