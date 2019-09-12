package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.dto.NoteDTO;

public interface UserNoteService {
	String createNote(NoteDTO validNote);

	String updateNote(NoteDTO validNote);

	String deleteNote(NoteDTO validNote);

	List<NoteDTO> showNoteList(NoteDTO validateNote);

}
