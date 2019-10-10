package com.bridgelabz.fundonotes.service;

import java.time.LocalDateTime;
import java.util.List;

import com.bridgelabz.fundonotes.dto.UserNoteValidation;
import com.bridgelabz.fundonotes.model.UserNotes;

public interface Note {
	
	UserNotes createNote(UserNoteValidation userNoteDto,String token);

	UserNotes updateNote(UserNotes userNotes, String token);

	UserNotes deleteNote(Long noteId, String token);

	List<UserNotes> showNoteList(String token);

	Boolean isPinned(String token, Long noteId);
	
	Boolean isArchive(String token,Long noteId);
	
	Boolean isTrash(String token,Long noteId);

	UserNotes updateReminder(LocalDateTime reminderDate, String token, Long noteId);

}