package com.ncuz.backend.controller;


import com.ncuz.backend.dto.AuthorizationDTO;
import com.ncuz.backend.dto.JwtRefreshTokenDTO;
import com.ncuz.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthService {

    private final AuthService authService;

    @Override
    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthorizationDTO authenticationRequest) throws Exception {
        return authService.login(authenticationRequest);
    }

    @Override
    @CrossOrigin(origins = "*")
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,@RequestBody JwtRefreshTokenDTO refreshToken) {

        return authService.refreshToken(request,refreshToken);
    }

}
