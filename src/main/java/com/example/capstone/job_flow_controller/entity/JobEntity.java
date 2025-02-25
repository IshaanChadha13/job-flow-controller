package com.example.capstone.job_flow_controller.entity;


import com.example.capstone.job_flow_controller.model.JobCategory;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="jobs")
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The "jobId" from the external system or message (often eventId)
    @Column(name = "external_job_id", unique = true)
    private String externalJobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_category", nullable = false)
    private JobCategory jobCategory;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Lob
    @Column(name = "payload", columnDefinition = "LONGTEXT")
    private String payload;

    @Column(name = "destination_topic")
    private String destinationTopic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status = JobStatus.NEW;

    @Column(name = "timestamp_created", nullable = false)
    private LocalDateTime timestampCreated = LocalDateTime.now();

    @Column(name = "timestamp_updated", nullable = false)
    private LocalDateTime timestampUpdated = LocalDateTime.now();

    // Constructors, getters, setters...
    @PreUpdate
    public void onUpdate() {
        timestampUpdated = LocalDateTime.now();
    }

    // No-args constructor
    public JobEntity() {}

    public JobEntity(String externalJobId, JobCategory jobCategory, Long tenantId, String payload) {
        this.externalJobId = externalJobId;
        this.jobCategory = jobCategory;
        this.tenantId = tenantId;
        this.payload = payload;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalJobId() {
        return externalJobId;
    }

    public void setExternalJobId(String externalJobId) {
        this.externalJobId = externalJobId;
    }

    public JobCategory getJobCategory() {
        return jobCategory;
    }

    public void setJobCategory(JobCategory jobCategory) {
        this.jobCategory = jobCategory;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getDestinationTopic() {
        return destinationTopic;
    }

    public void setDestinationTopic(String destinationTopic) {
        this.destinationTopic = destinationTopic;
    }

    public LocalDateTime getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(LocalDateTime timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public LocalDateTime getTimestampUpdated() {
        return timestampUpdated;
    }

    public void setTimestampUpdated(LocalDateTime timestampUpdated) {
        this.timestampUpdated = timestampUpdated;
    }
}
