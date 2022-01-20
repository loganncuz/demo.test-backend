package com.ncuz.backend.service;

import com.ncuz.backend.entity.Users;
import org.springframework.http.ResponseEntity;

public interface PublicService {
    ResponseEntity registerUser(Users user);/**Declare your own parameter**/
}
