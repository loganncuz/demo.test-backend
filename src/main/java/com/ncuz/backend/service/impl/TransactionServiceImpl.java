package com.ncuz.backend.service.impl;

import com.ncuz.backend.config.JwtToken;
import com.ncuz.backend.dto.BalanceDTO;
import com.ncuz.backend.dto.UserBalanceDTO;
import com.ncuz.backend.entity.UserBalance;
import com.ncuz.backend.entity.Users;
import com.ncuz.backend.payload.UserBalancePayload;
import com.ncuz.backend.repository.UserBalanceRepository;
import com.ncuz.backend.repository.UsersRepository;
import com.ncuz.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Isolation.READ_UNCOMMITTED;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;


@Service
@Transactional(propagation=REQUIRED,isolation=READ_UNCOMMITTED)
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private JwtToken jwtTokenUtil;
    @Autowired
    UserBalanceRepository userBalanceRepository;
    @Autowired
    UsersRepository usersRepository;
    @Override
    public ResponseEntity<?> depositBalance(BalanceDTO balanceDTO) {
        System.out.println("depositBalance authentication :"+jwtTokenUtil.getPrincipal());
        Users users=usersRepository.findByUsername(jwtTokenUtil.getPrincipal());
        UserBalance userBalance=userBalanceRepository.findByID(users);
        System.out.println("depositBalance authentication :"+userBalance.getUsers().getUserName()+" | "+balanceDTO.getAmount());
        userBalance.setBalance(userBalance.getBalance()+balanceDTO.getAmount());
        userBalanceRepository.saveAndFlush(userBalance);
        UserBalanceDTO userBalanceDTO=userBalanceRepository.getUserBalanceByName(jwtTokenUtil.getPrincipal());
        return ResponseEntity.ok(new UserBalancePayload(jwtTokenUtil.getPrincipal(),"Deposit",userBalance.getBalance(),"Transaction Successfully"));
    }

    @Override
    public ResponseEntity<?> payment(BalanceDTO balanceDTO) {
        System.out.println("payment authentication :"+jwtTokenUtil.getPrincipal());
        Users users=usersRepository.findByUsername(jwtTokenUtil.getPrincipal());
        UserBalance userBalance=userBalanceRepository.findByID(users);
        if(userBalance.getBalance()<balanceDTO.getAmount()){
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UserBalancePayload(jwtTokenUtil.getPrincipal(),"Payment",userBalance.getBalance(),
                            "Insufficient Amount, Transaction Failed"));
        }
        userBalance.setBalance(userBalance.getBalance()-balanceDTO.getAmount());
        userBalanceRepository.saveAndFlush(userBalance);
        return ResponseEntity.ok(new UserBalancePayload(jwtTokenUtil.getPrincipal(),"Deposit",userBalance.getBalance(),"Transaction Successfully"));
    }

    @Override
    public ResponseEntity<?> getBalance() {
        System.out.println("getBalance authentication :"+jwtTokenUtil.getPrincipal());
        UserBalanceDTO userBalanceDTO=userBalanceRepository.getUserBalanceByName(jwtTokenUtil.getPrincipal());
        System.out.println("getBalance authentication :"+userBalanceDTO.getUserName()+" | "+userBalanceDTO.getBalance());
        return ResponseEntity.ok(userBalanceDTO);
    }


}
