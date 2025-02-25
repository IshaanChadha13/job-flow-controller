package com.example.capstone.job_flow_controller.service;

import com.example.capstone.job_flow_controller.model.JobCategoryLimitEntity;
import com.example.capstone.job_flow_controller.model.TenantLimitEntity;
import com.example.capstone.job_flow_controller.repository.JobCategoryLimitRepository;
import com.example.capstone.job_flow_controller.repository.TenantLimitRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConcurrencyDbService {

    private final JobCategoryLimitRepository jobCategoryLimitRepo;
    private final TenantLimitRepository tenantLimitRepo;

    public ConcurrencyDbService(JobCategoryLimitRepository jobCategoryLimitRepo,
                                TenantLimitRepository tenantLimitRepo) {
        this.jobCategoryLimitRepo = jobCategoryLimitRepo;
        this.tenantLimitRepo = tenantLimitRepo;
    }

    public int getJobLimit(String jobCategory) {
        Optional<JobCategoryLimitEntity> opt = jobCategoryLimitRepo.findById(jobCategory);
        return opt.map(JobCategoryLimitEntity::getConcurrencyLimit).orElse(5);
    }

    public int getTenantLimit(Long tenantId) {
        Optional<TenantLimitEntity> opt = tenantLimitRepo.findById(tenantId);
        return opt.map(TenantLimitEntity::getConcurrencyLimit).orElse(2);
    }
}
