package com.example.MiniProject.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class IdCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String standard;

    private String section;

    private String address;

    @OneToOne
    @JoinColumn(name = "stud_id", nullable = false, unique = true)
    private Student student;

}
