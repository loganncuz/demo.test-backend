package com.ncuz.backend.config;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtToken jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try{
            String jwtToken = extractJwtFromRequest(request);
            if (StringUtils.hasText(jwtToken) && jwtTokenUtil.validateToken(jwtToken)) {
                UserDetails userDetails = new User(jwtTokenUtil.getUsernameFromToken(jwtToken), "",
                        jwtTokenUtil.getRolesFromToken(jwtToken));

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                System.out.println("Cannot set the Security Context");
            }
        }catch(ExpiredJwtException ex)
        {
            String isRefreshToken = "true";
            String requestURL = request.getRequestURL().toString();
            System.out.println("doFilterInternal requestURL :"+requestURL);
            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refresh")) {
                jwtTokenUtil.allowForRefreshToken(ex, request);
            } else
                request.setAttribute("exception", ex);
        }
        catch(BadCredentialsException ex)
        {
            request.setAttribute("exception", ex);
        }
        chain.doFilter(request, response);
    }



    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
