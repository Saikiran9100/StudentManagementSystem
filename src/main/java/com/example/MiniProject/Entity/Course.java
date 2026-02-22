package com.example.MiniProject.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses",uniqueConstraints = {@UniqueConstraint(columnNames = "course_name")})
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(name = "course_name",nullable = false,unique = true)
    private String courseName;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students=new HashSet<>();
}
