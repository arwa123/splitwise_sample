package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "net_balance")
    Float netBalance;

    @Column(name = "created_at")
    Date createdAT = new Date();
}
