package com.example.MiniProject.Entity.base;

import com.example.MiniProject.config.TenantContext;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "tenant_id")
    private String tenantId;

    @PrePersist
    public void setTenantId(){
        this.tenantId = TenantContext.getTenant();
    }
}