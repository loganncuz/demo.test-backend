package com.ncuz.backend.payload;

import com.ncuz.backend.entity.Users;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class UsersPayload implements Serializable {
    private String userName;
    private String message;

    public UsersPayload(Users users, String message) {
        this.userName=users.getUserName();
        this.message=message;
    }
}
