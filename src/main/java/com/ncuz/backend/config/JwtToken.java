package com.ncuz.backend.config;


import com.ncuz.backend.payload.JwtTokenPayload;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

@Configuration
public class JwtToken implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;

    private String secret;
    private int jwtExpirationInMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }



    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
        System.out.println("generateToken :"+roles);
        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            claims.put("isAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            claims.put("isUser", true);
        }
        String access_token=doGenerateToken(claims, userDetails.getUsername(),false);

        return access_token;
    }

    public JwtTokenPayload generateJWTToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
String u="ll";
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
//        System.out.println("generateToken :"+roles);
        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            claims.put("isAdmin", true);
            claims.put("isRefreshToken",false);
        }
        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            claims.put("isUser", true);
            claims.put("isRefreshToken",true);

        }
        JwtTokenPayload jwtResponse=new JwtTokenPayload();
        String accessToken=doGenerateToken(claims, userDetails.getUsername(),false);
        Claims expClaims= Jwts.parser().setSigningKey(secret).parseClaimsJws(accessToken).getBody();
        Long iat = new Long((Integer) expClaims.get("iat"));
        Long exp = new Long((Integer) expClaims.get("exp"));
        jwtResponse.setAccess_token(accessToken);
        jwtResponse.setExpired_in((exp-iat)*1000);

        String refresh_token=doGenerateToken(claims, userDetails.getUsername(),true);

        System.out.println("claims :"+expClaims);
        jwtResponse.setRefresh_token(refresh_token);

        return jwtResponse;
    }

    private String doGenerateToken(Map<String, Object> claims, String subject,Boolean isRefresh) {
        int forCast=(isRefresh)?2:1;
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (jwtExpirationInMs*forCast)))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    public boolean validateToken(String authToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            System.out.println("validateToken :"+claims);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            System.out.println("SignatureException :"+ex);
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            System.out.println("ExpiredJwtException :"+ex);
            throw ex;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();

    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        List<SimpleGrantedAuthority> roles = null;

        Boolean isAdmin = (claims.get("isAdmin", Boolean.class)==null)?false:claims.get("isAdmin", Boolean.class);
        Boolean isUser = (claims.get("isUser", Boolean.class)==null)?false:claims.get("isUser", Boolean.class);

        if (isAdmin != null && isAdmin) {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        if (isUser != null && isUser) {
            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return roles;

    }
    public void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        System.out.println("allowForRefreshToken :"+ex.getClaims());
        request.setAttribute("claims", ex.getClaims());

    }
    public String getPrincipal(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
