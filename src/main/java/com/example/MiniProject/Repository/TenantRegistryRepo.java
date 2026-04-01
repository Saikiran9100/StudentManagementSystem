package com.example.MiniProject.Repository;

import com.example.MiniProject.Entity.TenantRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantRegistryRepo extends JpaRepository<TenantRegistry, Long> {

    boolean existsByTenantIdIgnoreCase(String tenantId);

    List<TenantRegistry> findAllByOrderByTenantIdAsc();
}
