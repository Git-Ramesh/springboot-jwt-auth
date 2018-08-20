package com.rs.springbootjwtauth.config;

import com.rs.springbootjwtauth.filter.JWTAuthFilter;
import com.rs.springbootjwtauth.util.JwtTokenUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
public class JwtAuthFilterConfig extends WebSecurityConfigurerAdapter {
    private JwtTokenUtil jwtTokenUtil;

    public JwtAuthFilterConfig(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTAuthFilter tokenFilter = new JWTAuthFilter(jwtTokenUtil);
        http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
