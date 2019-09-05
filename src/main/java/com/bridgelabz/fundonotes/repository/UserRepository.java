package com.bridgelabz.fundonotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.fundonotes.dto.ValidateUser;
import com.bridgelabz.fundonotes.model.UserDetails;

public interface UserRepository extends JpaRepository<UserDetails, Integer> {
	

}
