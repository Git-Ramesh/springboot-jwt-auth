package com.rs.springbootjwtauth.repository;

import com.rs.springbootjwtauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
