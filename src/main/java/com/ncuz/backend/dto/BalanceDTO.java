package com.ncuz.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class BalanceDTO implements Serializable {
    private Double amount;
}
