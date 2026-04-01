package com.example.MiniProject.Entity;
import com.example.MiniProject.Entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Student extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private boolean active;
    private LocalDate admissionDate;
    private LocalDate graduationDate;
    private LocalDate lastDepartmentChangeDate;
    private Float cGpa;
    @OneToOne(mappedBy = "student",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private IdCard idCard;

    @OneToOne(mappedBy = "student",
    cascade = CascadeType.ALL,
    orphanRemoval = true)
    private BankAccount bankAccount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id",nullable = true)
    @JsonBackReference
    Department department;


    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses=new HashSet<>();



}
