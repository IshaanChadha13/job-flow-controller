package com.example.capstone.job_flow_controller.model;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConcurrencyConfig {

    private final Map<JobCategory, Integer> jobTypeLimits = new HashMap<>();

    private final Map<Long, Integer> tenantLimits = new HashMap<>();

    public ConcurrencyConfig() {
        jobTypeLimits.put(JobCategory.SCAN_PULL_CODESCAN, 4);
        jobTypeLimits.put(JobCategory.SCAN_PULL_DEPENDABOT, 4);
        jobTypeLimits.put(JobCategory.SCAN_PULL_SECRETSCAN, 4);
        jobTypeLimits.put(JobCategory.SCAN_PARSE_CODESCAN, 4);
        jobTypeLimits.put(JobCategory.SCAN_PARSE_DEPENDABOT, 4);
        jobTypeLimits.put(JobCategory.SCAN_PARSE_SECRETSCAN, 4);
        jobTypeLimits.put(JobCategory.UPDATE_FINDING, 4);


        // Tenants: 1, 2, 3 => limit = 2 each
        tenantLimits.put(1L, 2);
        tenantLimits.put(2L, 2);
        tenantLimits.put(3L, 2);
    }

    public int getJobLimit(JobCategory category) {
        return jobTypeLimits.getOrDefault(category, 5);
    }

    public int getTenantLimit(Long tenantId) {
        // default to 2 if not found
        return tenantLimits.getOrDefault(tenantId, 2);
    }
}
