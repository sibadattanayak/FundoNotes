
package com.bridgelabz.fundonotes.service;

import java.util.List;

import com.bridgelabz.fundonotes.model.NoteModel;


public interface ElasticSearchService {
	String createNote(NoteModel userNote);

	String updateNote(NoteModel note);

	String deleteNote(NoteModel note);

	List<NoteModel> searchNoteByData(String searchData);
}
