package com.rs.springbootjwtauth.service;

import com.rs.springbootjwtauth.model.User;
import com.rs.springbootjwtauth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllAvailableUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public User getUser(String username) {
       User user = userRepository.findByUsername(username);
       return user;
    }

    @Override
    public String deleteUser(User user) {
        userRepository.delete(user);
        return "User hasbeen removed";
    }
}
