package com.rs.springbootjwtauth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class  JwtTokenUtil {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-time:3600000}")
    private long validityInMilliseconds = 3600000;
    @Autowired
    private UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        //secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, List<String> roles) {
        System.out.println("Creating token..");
        Claims claims = Jwts.claims().setSubject(username);
//        claims.put("auth", roles.stream()
//                .map(s -> new SimpleGrantedAuthority(s.getAuthority()))
//                .filter(Objects::nonNull).collect(Collectors.toList()));
        claims.put("auth",
                roles.stream()
                        .filter(Objects::nonNull)
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    public Authentication getAuthentication(String token) {
        System.out.println("Getting the authentication from token");
        UserDetails userDetails =userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        System.out.println("Getting the username from token..");
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        System.out.println("Checking Bearer token from req header..");
        String bearerToken = req.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
    public boolean validateToken(String token) {
        System.out.println("Validating the token..");
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            System.out.println("Token valid..");
            return true;
        } catch(JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }
}
