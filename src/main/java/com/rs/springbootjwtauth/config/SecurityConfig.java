package com.rs.springbootjwtauth.config;

import com.rs.springbootjwtauth.filter.JWTAuthFilter;
import com.rs.springbootjwtauth.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Disable CSRF (cross site request forgery)
        http.csrf().disable();
        http.addFilterBefore(new JWTAuthFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);
        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //Entry points
        http.authorizeRequests()
                .antMatchers("/user/**").hasRole("ADMIN")
                .antMatchers("/authenticate").permitAll()
                .antMatchers("/**").permitAll()
                // Disallow everything else..
                .anyRequest().authenticated();
        // If a user try to access a resource without having enough permissions
        http.exceptionHandling().accessDeniedPage("/login");
        // Apply JWT
       // http.apply(new JwtAuthFilterConfig(jwtTokenUtil));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Allow swagger to be accessed without authentication
        web.ignoring().antMatchers("/v2/api-docs")//
                .antMatchers("/swagger-resources/**")//
                .antMatchers("/swagger-ui.html")//
                .antMatchers("/configuration/**")//
                .antMatchers("/webjars/**")//
                .antMatchers("/public")
        // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**");;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
