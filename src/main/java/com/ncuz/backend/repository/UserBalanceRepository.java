package com.ncuz.backend.repository;
import com.ncuz.backend.dto.UserBalanceDTO;
import com.ncuz.backend.entity.UserBalance;
import com.ncuz.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserBalanceRepository extends JpaRepository<UserBalance, Integer> {
    @Query("SELECT DISTINCT u FROM UserBalance u WHERE u.users = :users")
    UserBalance findByID(@Param("users") Users users);
    @Query(value = "SELECT identityNo,userName,balance FROM users u " +
            "join users_balance ub on u.id=ub.users_ID "+
            "where u.userName=:username",
            nativeQuery = true)
    UserBalanceDTO getUserBalanceByName(@Param("username") String username);
}
