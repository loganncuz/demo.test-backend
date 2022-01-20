package com.ncuz.backend.entity;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name="users", uniqueConstraints = @UniqueConstraint(columnNames = {"username","identityNo"}, name="USER_UNIQUE_USERNAME"))
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint unsigned")
    private Integer id;
    @Column(name = "username")
    private String userName;
    @Column(name = "password")
    private String password;
    @Column(name = "birthDate")
    private String birthDate;
    @Column(name = "phone")
    private String phone;
    @Column(name = "identityNo")
    private String identityNo;
    @Column
    private String role;

    @OneToOne(mappedBy = "users")
    private UserBalance userBalance;
}
