package com.notahmed.springsecurityexample.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


// the class will extends JpaRepository so similar to JDBCRepository


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
