package com.rs.springbootjwtauth.service;

import com.rs.springbootjwtauth.model.User;

import java.util.List;

public interface UserService {
    public List<User> getAllAvailableUsers();
    public User getUser(String username);
    public String deleteUser(User user);
}
