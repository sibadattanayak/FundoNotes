package com.bridgelabz.fundonotes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNotes;

@Repository
public interface UserDataRepository extends JpaRepository<UserDetails, Integer> {

	Optional<UserDetails> findByEmail(String email);

	
	public static final String FIND_PROJECTS = "SELECT user_firstName FROM user_details";

	@Query(value = FIND_PROJECTS, nativeQuery = true)
	public List<UserDetails> findFirstName();
	
	
	@Query("User_FirstName from userDetails where id=?1")
	Optional<UserNotes> findByNoteId(int noteId);

}
