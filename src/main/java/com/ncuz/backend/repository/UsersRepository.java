package com.ncuz.backend.repository;

import com.ncuz.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT DISTINCT u FROM Users u WHERE u.userName = :username")
    Users findByUsername(@Param("username") String username);
}
