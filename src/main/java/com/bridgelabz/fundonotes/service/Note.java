package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.UserNoteValidation;
import com.bridgelabz.fundonotes.model.UserNotes;

public interface Note {
	
	UserNotes createNote(String noteData,String token);

	UserNotes updateNote(Long noteId, String token);

	String deleteNote(Long noteId, String token);

	List<UserNoteValidation> showNoteList(UserNoteValidation validateNote);
}