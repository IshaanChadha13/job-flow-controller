package com.example.capstone.job_flow_controller.entity;

public enum JobStatus {

    NEW,         // Received, but not scheduled yet
    READY,       // Selected by scheduler, about to be published
    IN_PROGRESS, // Sent to the next microservice
    SUCCESS,     // Completed successfully
    FAILURE
}
