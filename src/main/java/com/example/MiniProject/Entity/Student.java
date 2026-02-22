package com.example.MiniProject.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;

    @OneToOne(mappedBy = "student",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private IdCard idCard;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id",nullable = true)
    Department department;


    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses=new HashSet<>();



}
