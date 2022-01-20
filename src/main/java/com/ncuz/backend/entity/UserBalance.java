package com.ncuz.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="users_balance", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}, name="USER_UNIQUE_USERNAME"))
public class UserBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", columnDefinition = "bigint unsigned")
    private Integer id;
    @Column(name = "balance")
    private Double balance;
    @OneToOne
    private Users users;
}
