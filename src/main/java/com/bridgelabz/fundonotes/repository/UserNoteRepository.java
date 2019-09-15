package com.bridgelabz.fundonotes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNotes;
@Repository
public interface UserNoteRepository extends JpaRepository<UserNotes, Long> {

	//@Query("FROM UserNotes WHERE noteId = ?1")
	Optional<UserNotes> findByNoteId(Long noteId);

	//@Query("FROM UserNotes WHERE noteId = ?1")
	Optional<UserNotes> deleteByNoteId(Long noteId);

}
