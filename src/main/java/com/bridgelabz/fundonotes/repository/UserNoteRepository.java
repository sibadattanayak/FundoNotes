package com.bridgelabz.fundonotes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNotes;

@Repository
public interface UserNoteRepository extends JpaRepository<UserNotes, Long> {

	@Query(value = "SELECT collabrator_note_list_user_note_id FROM user_notes_collabrator_user_list WHERE collabrator_user_list_user_id = ?1", nativeQuery = true)
	List<Long> findBycollabratorUserList(Long id);

	@Query(value = "SELECT collabrator_user_list_user_id FROM user_notes_collabrator_user_list WHERE collabrator_note_list_user_note_id = ?1", nativeQuery = true)
	List<Long> findBycollabratorNoteId(Long id); 	
}
