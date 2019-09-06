package com.bridgelabz.fundonotes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bridgelabz.fundonotes.model.UserDetails;

public interface UserRepository extends JpaRepository<UserDetails, Integer> {

	 Optional<UserDetails> findByEmail(String email);
}
