package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.UserNoteLabelInfoDTO;
import com.bridgelabz.fundonotes.model.LabelModel;

public interface Label {

	List<LabelModel> createLabel(UserNoteLabelInfoDTO labelDto, String token);

	LabelModel updateLabel(UserNoteLabelInfoDTO noteLabelValidation, String token);

	void deleteLabel(Long labelId, String token);

	List<LabelModel> showLabelList(String token);

}
