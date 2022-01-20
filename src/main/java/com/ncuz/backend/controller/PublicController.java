package com.ncuz.backend.controller;


import com.ncuz.backend.entity.Users;
import com.ncuz.backend.service.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController implements PublicService {

    private final PublicService publicService;

    @Override
    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody Users user) {
        return publicService.registerUser(user);
    }
}
