package com.bridgelabz.fundonotes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNotes;

public interface UserRepository extends JpaRepository<UserDetails, Integer> {

	Optional<UserDetails> findByEmail(String email);

	@Query("email from userDetails where id=?1")
	Optional<UserNotes> findByNoteId(int noteId);

}
