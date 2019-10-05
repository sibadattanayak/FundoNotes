package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.UserNoteValidation;
import com.bridgelabz.fundonotes.model.UserNotes;

public interface Note {
	
	UserNotes createNote(UserNoteValidation userNoteDto,String token);

	UserNotes updateNote(UserNoteValidation userNoteDto, String token, Long userNoteId);

	UserNotes deleteNote(Long noteId, String token);

	List<UserNoteValidation> showNoteList(UserNoteValidation validateNote);
}