package com.chatApplication.chatapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatApplication.chatapp.model.MyAppUser;

@Repository
public interface MyAppUserRepository extends JpaRepository<MyAppUser, Long> {
    
    Optional<MyAppUser> findByUsername(String username);    
    Optional<MyAppUser> findByEmail(String email);
}
