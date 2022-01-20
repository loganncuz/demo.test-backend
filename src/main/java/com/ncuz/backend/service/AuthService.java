package com.ncuz.backend.service;

import com.ncuz.backend.dto.AuthorizationDTO;
import com.ncuz.backend.dto.JwtRefreshTokenDTO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    ResponseEntity login(AuthorizationDTO authenticationRequest) throws Exception; /**Declare your own parameter**/
    ResponseEntity refreshToken(HttpServletRequest request, JwtRefreshTokenDTO refreshToken); /**Declare your own parameter**/
}
