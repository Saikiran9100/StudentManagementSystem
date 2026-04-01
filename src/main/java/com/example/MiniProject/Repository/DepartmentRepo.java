package com.example.MiniProject.Repository;


import com.example.MiniProject.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepo extends JpaRepository<Department,Long>, JpaSpecificationExecutor<Department> {
    boolean existsByDeptCode(String deptCode);

    boolean existsByDeptNameIgnoreCase(String deptName);
    Optional<Department> findByDeptIdAndTenantId(Long deptId, String tenantId);

    List<Department> findAllByTenantId(String tenantId);

    boolean existsByDeptNameAndTenantId(String deptName, String tenantId);

    void deleteByDeptIdAndTenantId(Long deptId, String tenantId);

    boolean existsByDeptNameIgnoreCaseAndTenantId(String deptName, String tenantId);

    boolean existsByDeptCodeAndTenantId(String deptCode, String tenantId);

    @Query("select distinct d.tenantId from Department d where d.tenantId is not null and d.tenantId <> ''")
    List<String> findDistinctTenantIds();
}
