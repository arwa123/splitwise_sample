package com.company.entity;

import com.company.enums.TransactionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    Long id;

    @Column(name = "type")
    TransactionType type;

    @Column(name = "amount")
    Float amount;

    @Column(name = "from_user_id")
    Long fromUserId;

    @Column(name = "to_user_id")
    Long toUserId;

    @Column(name = "created_at")
    Date createdAt = new Date();

    @Column(name = "created_by")
    String createdBy;

}
