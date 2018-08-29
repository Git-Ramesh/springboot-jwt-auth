package com.rs.springbootjwtauth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rs.springbootjwtauth.model.User;
import com.rs.springbootjwtauth.repository.UserRepository;
import com.rs.springbootjwtauth.service.UserService;
import com.rs.springbootjwtauth.util.JwtTokenUtil;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        User user = userService.getUser(username);
        if(user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/user/all")
    public ResponseEntity<List<User>> getAvailableUsers() {
        List<User> users = userService.getAllAvailableUsers();
        if(users == null) {
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @PostMapping("/authenticate")
    public String token(HttpServletRequest req) throws IOException {
        String token  = null;
        String result = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
        System.out.println(result);
        JSONObject jsonUsernameAndPassword = new JSONObject(result);
        JSONObject resp = new JSONObject();
        String username = jsonUsernameAndPassword.getString("username");
        String password = jsonUsernameAndPassword.getString("password");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if(userDetails != null && userDetails.getPassword().equals(password)) {
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            if(auth.isAuthenticated()) {
               token = jwtTokenUtil.createToken(userDetails.getUsername(), userDetails.getAuthorities().stream().map(authority->((GrantedAuthority) authority).getAuthority()).collect(Collectors.toList()));
            }
        }
        resp.put("username" , username);
        resp.put("authorities", userDetails.getAuthorities().stream().map(auth -> ((GrantedAuthority) auth).getAuthority()).collect(Collectors.toList()));
        resp.put("token", token);

        return resp.toString();
    }
    @DeleteMapping("user/delete")
    public void deleteUser(@RequestBody User user) {
        userService.deleteUser(user);
    }
    @PostMapping("/register")
    public User saveUser(@RequestBody User user) {
       return  userRepository.save(user);
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }
}
