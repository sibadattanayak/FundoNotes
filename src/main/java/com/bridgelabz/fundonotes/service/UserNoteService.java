package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.NoteDTO;

public interface UserNoteService {
	
	void createNote(String noteData,String token);

	void updateNote(int noteId, String token);

	void deleteNote(int noteId, String token);

	List<NoteDTO> showNoteList(NoteDTO validateNote);
}