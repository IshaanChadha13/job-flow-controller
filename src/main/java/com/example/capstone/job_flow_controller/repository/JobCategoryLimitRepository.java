package com.example.capstone.job_flow_controller.repository;

import com.example.capstone.job_flow_controller.model.JobCategoryLimitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCategoryLimitRepository extends JpaRepository<JobCategoryLimitEntity, String> {
    // "String" is the PK type -> jobCategory
}

