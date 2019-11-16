package com.bridgelabz.fundonotes.service;

import java.time.LocalDateTime;
import java.util.List;

import com.bridgelabz.fundonotes.dto.UserLoginDTO;
import com.bridgelabz.fundonotes.dto.UserNoteLabelInfoDTO;
import com.bridgelabz.fundonotes.dto.UserNoteInfoDTO;
import com.bridgelabz.fundonotes.model.NoteModel;

public interface Note {

	List<NoteModel> createNote(UserNoteInfoDTO userNoteDto, String token);

	NoteModel updateNote(NoteModel noteModel, String token);

	NoteModel deleteNote(Long noteId, String token);

	List<NoteModel> showNoteList(String token);

	Boolean isPinned(String token, Long noteId);

	Boolean isArchive(String token, Long noteId);

	Boolean isTrash(String token, Long noteId);

	NoteModel updateReminder(LocalDateTime reminderDate, String token, Long noteId);

	String addCollaborater(String token, String email, Long noteId);

	String removeCollaborater(String token, String email, Long noteId);

	NoteModel updateColor(String color, String token, Long noteId);

	List<NoteModel> getReminder(String token);

	List<NoteModel> getTrash(String token);

	List<NoteModel> getArchivedNotes(String token);

	List<String> getAllCollaborators(Long noteId, String token);

	List<NoteModel> getNotesOnLabel(Long labelId, String token);

	public String listLabel(String token, Long noteId, UserNoteLabelInfoDTO labeldto);

}