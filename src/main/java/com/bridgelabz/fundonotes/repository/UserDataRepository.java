package com.bridgelabz.fundonotes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundonotes.model.UserDetails;

@Repository
public interface UserDataRepository extends JpaRepository<UserDetails, Long> {

	Optional<UserDetails> findByEmail(String email);

}
