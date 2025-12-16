package com.crp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crp.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	
//	 Optional<User> findByEmail(String email);
	 
	 @Query("SELECT u FROM User u WHERE u.email = :email")
	 Optional<User> findUserByEmail(@Param("email") String email);
	 
	 User findByEmail(String email);
	 boolean existsByEmail(String email);

	
}
