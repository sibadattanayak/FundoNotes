package com.bridgelabz.fundonotes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.model.LabelModel;
@Repository
public interface UserNoteLabelRepository extends JpaRepository<LabelModel, Long> {

	
	@Query(value="select note_model_user_note_id from user_notes_label where label_label_id =?1" , nativeQuery=true)
	List<Long> findByLabelId(Long labelId);
}
