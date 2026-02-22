package com.example.MiniProject.Repository;


import com.example.MiniProject.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long> {
    boolean existsByDeptName(String deptName);
}
