package com.bridgelabz.fundonotes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNotes;

public interface UserNotesRepository extends JpaRepository<UserNotes, Integer> {

	@Query ("FROM UserDetails WHERE email = ?1")
	Optional<UserDetails> findByEmail(String email);

	@Query("FROM UserNotes WHERE noteId = ?1")
	Optional<UserNotes> findByNoteId(int noteId);

	@Query("FROM UserNotes WHERE noteId = ?1")
	Optional<UserNotes> deleteByNoteId(int noteId);

}
