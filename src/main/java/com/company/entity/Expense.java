package com.company.entity;

import com.company.enums.ExpenseType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "expenses")
@Getter
@Setter
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    Long id;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "amount")
    Float amount;

    @Column(name = "user_ids")
    String toUserIds;

    @Column(name = "group_id")
    Long groupId;

    @Column(name = "type")
    ExpenseType type;


    @Column(name = "created_at")
    Date createdAT = new Date();


}
