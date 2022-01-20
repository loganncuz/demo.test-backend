package com.ncuz.backend.payload;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserBalancePayload implements Serializable {
    private String userName;
    private String message;
    private Double saldo;
    private String transactionType;

    public UserBalancePayload(String principal, String transactionType, Double balance, String message) {
        this.userName=principal;
        this.transactionType=transactionType;
        this.saldo=balance;
        this.message=message;
    }
}
