package com.rs.springbootjwtauth.filter;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.rs.springbootjwtauth.util.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthFilter extends GenericFilterBean {


    private JwtTokenUtil jwtTokenUtil;

    public JWTAuthFilter(JwtTokenUtil jwtTokenUtil) {
        System.out.println("JWTAuthFilter: 0-param constr");
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doFilter..");
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResp = (HttpServletResponse) servletResponse;

        String token = jwtTokenUtil.resolveToken(httpReq);
        System.out.println("token: " + token);
        try {
            if(token != null && jwtTokenUtil.validateToken(token)) {
                Authentication auth = token != null ? jwtTokenUtil.getAuthentication(token) : null;
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch(Exception e) {
            httpResp.sendError(403, e.getMessage());
        }
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("After doFilter");
    }
}
