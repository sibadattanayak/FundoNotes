package com.bridgelabz.fundonotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserNoteLabelRepository extends JpaRepository<UserNoteLabelRepository, Integer> {

}
