package com.example.capstone.job_flow_controller.repository;

import com.example.capstone.job_flow_controller.entity.JobEntity;
import com.example.capstone.job_flow_controller.entity.JobStatus;
import com.example.capstone.job_flow_controller.model.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<JobEntity, Long> {

    // Find all jobs by status & jobCategory
    List<JobEntity> findByJobCategoryAndStatus(JobCategory category, JobStatus status);

    // Count how many jobs of a category are in progress
    int countByJobCategoryAndStatus(JobCategory category, JobStatus status);

    // Count how many jobs are in progress for a category AND a specific tenant
    int countByJobCategoryAndTenantIdAndStatus(JobCategory category, Long tenantId, JobStatus status);

    // Find by externalJobId
    JobEntity findByExternalJobId(String externalJobId);
}
