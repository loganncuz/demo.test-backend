package com.ncuz.backend.service.impl;


import com.ncuz.backend.entity.UserBalance;
import com.ncuz.backend.entity.Users;
import com.ncuz.backend.payload.UsersPayload;
import com.ncuz.backend.repository.UserBalanceRepository;
import com.ncuz.backend.repository.UsersRepository;
import com.ncuz.backend.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PublicServiceImpl implements PublicService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    UserBalanceRepository userBalanceRepository;
    @Override
    public ResponseEntity<Object> registerUser(Users user) {
        Users users = usersRepository.findByUsername(user.getUserName());
        if(users ==null){
            users=new Users();
            users.setUserName(user.getUserName());
            users.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            users.setBirthDate(user.getBirthDate());
            users.setPhone(user.getPhone());
            users.setRole(user.getRole());
            users.setIdentityNo(user.getIdentityNo());
            usersRepository.saveAndFlush(users);

            UserBalance userBalance = userBalanceRepository.findByID(users);
            if(userBalance ==null){
                userBalance=new UserBalance();
                userBalance.setUsers(users);
                userBalance.setBalance((double) 0);
                userBalanceRepository.saveAndFlush(userBalance);
            }
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UsersPayload(users,"User :"+users.getUserName().toUpperCase()+" is registered"));
        }else{
            throw new ResponseStatusException(HttpStatus.FOUND,"User :"+users.getUserName().toUpperCase()+" is already exist", new Exception());
        }
    }
}
