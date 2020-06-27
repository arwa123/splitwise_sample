package com.company.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "expense_groups")
@Getter
@Setter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    Long id;

    @Column(name = "name")
    String name;

    @Column(name = "user_ids")
    String userIds;

    @Column(name = "created_at")
    Date createdAT = new Date();

}
