package com.example.MiniProject.Repository;

import com.example.MiniProject.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepo extends JpaRepository<Student,Long> {
    List<Student> findByDepartment_DeptId(Long deptId);
}
