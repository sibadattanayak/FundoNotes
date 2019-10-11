package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.UserNoteLabelValidation;
import com.bridgelabz.fundonotes.model.UserNoteLabel;

public interface Label {

	List<UserNoteLabel> createLabel(UserNoteLabelValidation labelDto, String token);

	UserNoteLabel updateLabel(UserNoteLabelValidation noteLabelValidation, String token);

	void deleteLabel(Long labelId, String token);

	List<UserNoteLabel> showLabelList(String token);

}
