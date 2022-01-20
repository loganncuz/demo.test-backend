package com.ncuz.backend.payload;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class JwtTokenPayload implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private   String access_token;
    private   String refresh_token;
    private   String token_type= "Bearer";
    private   Long expired_in;


    public JwtTokenPayload(String token) {
        access_token=token;
    }

    public JwtTokenPayload() {

    }
}
