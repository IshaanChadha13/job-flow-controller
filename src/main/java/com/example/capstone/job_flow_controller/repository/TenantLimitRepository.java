package com.example.capstone.job_flow_controller.repository;

import com.example.capstone.job_flow_controller.model.TenantLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantLimitRepository extends JpaRepository<TenantLimitEntity, Long> {
    // "Long" is the PK type -> tenantId
}
