package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.UserNoteLabelValidation;
import com.bridgelabz.fundonotes.model.UserNoteLabel;

public interface Label {

	UserNoteLabel createLabel(String labelName, String token);

	UserNoteLabel updateLabel(UserNoteLabelValidation noteLabelValidation, String token);

	void deleteLabel(Long labelId, String token);

	List<UserNoteLabel> showNoteLabelList(UserNoteLabelValidation validateNoteLabel, String token);

}
