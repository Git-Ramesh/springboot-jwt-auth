package com.rs.springbootjwtauth.service;

import com.rs.springbootjwtauth.model.Role;
import com.rs.springbootjwtauth.model.User;
import com.rs.springbootjwtauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userDetailsService")
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByUsername(username);
        List<Role> roles = user.getRoles();
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                roles.stream().map(role -> role.getRole()).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}
