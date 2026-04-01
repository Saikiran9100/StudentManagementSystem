package com.example.MiniProject.Repository;

import com.example.MiniProject.Entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, Long>,
        JpaSpecificationExecutor<BankAccount> {

    boolean existsByAccountNo(String accountNo);

    boolean existsByAccountNoAndTenantId(String accountNo, String tenantId);

    List<BankAccount> findAllByTenantId(String tenantId);

    Optional<BankAccount> findByBankIdAndTenantId(Long bankId, String tenantId);

    @Query("select distinct b.tenantId from BankAccount b where b.tenantId is not null and b.tenantId <> ''")
    List<String> findDistinctTenantIds();

}
