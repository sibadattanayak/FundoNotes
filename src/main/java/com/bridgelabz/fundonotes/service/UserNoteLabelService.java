package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.NoteLabelDTO;

public interface UserNoteLabelService {

	List<NoteLabelDTO> showNoteLabelList(NoteLabelDTO validateNoteLabel);

}
