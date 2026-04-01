package com.example.MiniProject.Repository;


import com.example.MiniProject.Entity.IdCard;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IdCardRepo extends JpaRepository<IdCard,Long>, JpaSpecificationExecutor<IdCard> {

    Optional<IdCard> findByIdAndTenantId(Long id, String tenantId);

    List<IdCard> findAllByTenantId(String tenantId);

    boolean existsByCardNumberAndTenantId(String cardNumber, String tenantId);

    @Query("select distinct i.tenantId from IdCard i where i.tenantId is not null and i.tenantId <> ''")
    List<String> findDistinctTenantIds();

}
