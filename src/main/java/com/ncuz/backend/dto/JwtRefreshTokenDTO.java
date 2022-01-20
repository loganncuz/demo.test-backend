package com.ncuz.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class JwtRefreshTokenDTO implements Serializable {
    private String token;
}
