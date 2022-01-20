package com.ncuz.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class AuthorizationDTO implements Serializable {
    private String userName;
    private String password;

    public AuthorizationDTO() {

    }
    public AuthorizationDTO(String username, String password) {
        this.setUserName(username);
        this.setPassword(password);
    }

}
