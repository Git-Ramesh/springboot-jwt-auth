package com.rs.springbootjwtauth;

import com.rs.springbootjwtauth.model.Role;
import com.rs.springbootjwtauth.model.User;
import com.rs.springbootjwtauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackages = "com.rs.springbootjwtauth.model")
public class SpringbootJwtAuthApplication implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJwtAuthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
      /*  User user = new User();
        user.setActive(true);
        user.setUsername("ramesh");
        user.setPassword("12345678");
        user.setEmail("ramesh@gmail.com");
        user.setId(1);
        Role role1 = new Role(1,"ROLE_ADMIN");
        Role role2 = new Role(2,"ROLE_USER");
        user.setRoles(Arrays.asList(role1, role2));
        userRepository.save(user);*/
       /* User user = new User();
        user.setActive(true);
        user.setUsername("jaga");
        user.setPassword("12345678");
        user.setEmail("jaga@gmail.com");
        user.setId(1);
      //  Role role1 = new Role(1,"ROLE_ADMIN");
        Role role2 = new Role(2,"ROLE_USER");
        user.setRoles(Arrays.asList(role2));
        userRepository.save(user);*/
    }
}
