package com.bridgelabz.fundonotes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.model.UserDetailsModel;

@Repository
public interface UserDataRepository extends JpaRepository<UserDetailsModel, Long> {

	Optional<UserDetailsModel> findByEmail(String email);

}
