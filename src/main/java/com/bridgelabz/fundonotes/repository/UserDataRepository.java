package com.bridgelabz.fundonotes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNotes;

@Repository
public interface UserDataRepository extends JpaRepository<UserDetails, Long> {

	Optional<UserDetails> findByEmail(String email);

	//public List<UserDetails> findByFirstName();

//	Optional<UserNotes> findByNoteId(int noteId);

}
