package com.bridgelabz.fundonotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.model.UserNoteLabel;
@Repository
public interface UserNoteLabelRepository extends JpaRepository<UserNoteLabel, Integer> {

}
