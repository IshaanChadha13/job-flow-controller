package com.example.capstone.job_flow_controller.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tenant_limits")
public class TenantLimitEntity {

    @Id
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "concurrency_limit", nullable = false)
    private int concurrencyLimit;

    public TenantLimitEntity() {}

    public TenantLimitEntity(Long tenantId, int concurrencyLimit) {
        this.tenantId = tenantId;
        this.concurrencyLimit = concurrencyLimit;
    }

    public Long getTenantId() {
        return tenantId;
    }
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public int getConcurrencyLimit() {
        return concurrencyLimit;
    }
    public void setConcurrencyLimit(int concurrencyLimit) {
        this.concurrencyLimit = concurrencyLimit;
    }
}
