package com.example.capstone.job_flow_controller.service;

import com.example.capstone.job_flow_controller.model.JobCategory;
import org.springframework.stereotype.Service;

@Service
public class JobFlowService {

    public JobCategory determinePullJobCategory(String tool) {
        if ("CODE_SCANNING".equalsIgnoreCase(tool)) {
            return JobCategory.SCAN_PULL_CODESCAN;
        } else if ("DEPENDABOT".equalsIgnoreCase(tool)) {
            return JobCategory.SCAN_PULL_DEPENDABOT;
        } else if ("SECRET_SCANNING".equalsIgnoreCase(tool)) {
            return JobCategory.SCAN_PULL_SECRETSCAN;
        }
        throw new IllegalArgumentException("Unsupported tool: " + tool);
    }

    public JobCategory determineParseJobCategory(String tool) {
        if ("CODE_SCANNING".equalsIgnoreCase(tool)) {
            return JobCategory.SCAN_PARSE_CODESCAN;
        } else if ("DEPENDABOT".equalsIgnoreCase(tool)) {
            return JobCategory.SCAN_PARSE_DEPENDABOT;
        } else if ("SECRET_SCANNING".equalsIgnoreCase(tool)) {
            return JobCategory.SCAN_PARSE_SECRETSCAN;
        }
        throw new IllegalArgumentException("Unsupported tool: " + tool);
    }
}
