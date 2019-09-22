package com.bridgelabz.fundonotes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNotes;
@Repository
public interface UserNoteRepository extends JpaRepository<UserNotes, Long> {

	Optional<UserNotes> findByNoteId(Long noteId);

	Optional<UserNotes> deleteByNoteId(Long noteId);

}
