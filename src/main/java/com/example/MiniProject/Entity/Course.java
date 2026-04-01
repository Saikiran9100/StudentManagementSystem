package com.example.MiniProject.Entity;


import com.example.MiniProject.Entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses",uniqueConstraints = {@UniqueConstraint(columnNames = "course_name")})
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(name = "course_name",nullable = false)
    private String courseName;

    @Column(nullable = false)
    private Double courseFee;

    private LocalDate deadLine;

    private Integer maxCapacity;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students=new HashSet<>();
}
