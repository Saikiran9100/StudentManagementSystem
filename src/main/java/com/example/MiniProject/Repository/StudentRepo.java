package com.example.MiniProject.Repository;

import com.example.MiniProject.Entity.Department;
import com.example.MiniProject.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepo extends JpaRepository<Student,Long>, JpaSpecificationExecutor<Student> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndTenantId(String email, String tenantId);
    List<Student> findAllByTenantId(String tenantId);
    Optional<Student> findByStudIdAndTenantId(Long studId, String tenantId);
    @Query("select distinct s.tenantId from Student s where s.tenantId is not null and s.tenantId <> ''")
    List<String> findDistinctTenantIds();
}
