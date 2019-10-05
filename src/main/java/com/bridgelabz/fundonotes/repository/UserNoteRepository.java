package com.bridgelabz.fundonotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.model.UserDetails;
import com.bridgelabz.fundonotes.model.UserNotes;

@Repository
public interface UserNoteRepository extends JpaRepository<UserNotes, Long> {

}
