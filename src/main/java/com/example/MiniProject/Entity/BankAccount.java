package com.example.MiniProject.Entity;


import com.example.MiniProject.Entity.base.BaseEntity;
import com.example.MiniProject.converter.AccountNumberEncryptConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BankAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankId;

    @Column(unique = true)
    @Convert(converter = AccountNumberEncryptConverter.class)
    private String accountNo;
    private String bankName;
    private String branch;
    private Double balance;

    @OneToOne
    @JoinColumn(name = "stud_id",nullable = false,unique = true)
    private Student student;
}
