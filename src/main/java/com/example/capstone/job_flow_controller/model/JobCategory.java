package com.example.capstone.job_flow_controller.model;

public enum JobCategory {

    SCAN_PULL_CODESCAN,
    SCAN_PULL_DEPENDABOT,
    SCAN_PULL_SECRETSCAN,

    SCAN_PARSE_CODESCAN,
    SCAN_PARSE_DEPENDABOT,
    SCAN_PARSE_SECRETSCAN,

    UPDATE_FINDING,

    CREATE_TICKET,
    TRANSITION_TICKET,

    NEW_SCAN
}
