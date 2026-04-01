package com.example.MiniProject.Entity;


import com.example.MiniProject.Entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deptId;

    @Column(nullable = false, unique = true)
    private String deptName;
    @Column(nullable = false, unique = true)
    private String deptCode;

    @Column(nullable = false)
    private Long capacity;

    private Integer currentStrength;
    @OneToMany(mappedBy = "department")
    private List<Student> students = new ArrayList<>();

}
