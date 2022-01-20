package com.ncuz.backend.service.impl;


import com.ncuz.backend.config.JwtToken;
import com.ncuz.backend.dto.AuthorizationDTO;
import com.ncuz.backend.dto.CustomUserDetailsDTO;
import com.ncuz.backend.dto.JwtRefreshTokenDTO;
import com.ncuz.backend.entity.Users;
import com.ncuz.backend.payload.JwtTokenPayload;
import com.ncuz.backend.repository.UsersRepository;
import com.ncuz.backend.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class AuthServiceImpl implements AuthService,UserDetailsService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtToken jwtTokenUtil;
    @Autowired
    UsersRepository usersRepository;
    private String secret;
    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }
    @Override
    public ResponseEntity<?> login(AuthorizationDTO authenticationRequest) throws Exception {
        authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
        UserDetails userDetails = loadUserByUsername(authenticationRequest.getUserName());
        JwtTokenPayload jwtResponse=jwtTokenUtil.generateJWTToken(userDetails);


        return ResponseEntity.ok(jwtResponse);
    }

    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request, JwtRefreshTokenDTO refreshToken) {
        if(request.getAttribute("claims")==null){
            try {
                Jws<Claims>  expClaims = Jwts.parser().setSigningKey(secret).parseClaimsJws(refreshToken.getToken());
                request.setAttribute("claims",expClaims.getBody());
            }catch (ExpiredJwtException ex){
                jwtTokenUtil.allowForRefreshToken(ex,request);
            }
        }
        DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");
        Map<String, Object> expectedMap = getMapFromIoJsonWebTokenClaims(claims);
        String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(token);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(username);
        if (user != null) {
            CustomUserDetailsDTO customUserDetails = new CustomUserDetailsDTO();

            customUserDetails.setUserName(user.getUserName());
            customUserDetails.setPassword(user.getPassword());
            Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
            customUserDetails.setGrantedAuthorities(authorities);
            return customUserDetails;
        }
        throw new UsernameNotFoundException(username);
    }
    public void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            System.out.println("DisabledException :"+e);
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            System.out.println("BadCredentialsException :"+e);
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    public Map<String, Object> getMapFromIoJsonWebTokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }
}
