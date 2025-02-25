package com.example.capstone.job_flow_controller.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "job_category_limits")
public class JobCategoryLimitEntity {

    @Id
    @Column(name = "job_category", nullable = false)
    private String jobCategory;  // e.g. "SCAN_PULL_CODESCAN"

    @Column(name = "concurrency_limit", nullable = false)
    private int concurrencyLimit;

    public JobCategoryLimitEntity() {}

    public JobCategoryLimitEntity(String jobCategory, int concurrencyLimit) {
        this.jobCategory = jobCategory;
        this.concurrencyLimit = concurrencyLimit;
    }

    public String getJobCategory() {
        return jobCategory;
    }
    public void setJobCategory(String jobCategory) {
        this.jobCategory = jobCategory;
    }

    public int getConcurrencyLimit() {
        return concurrencyLimit;
    }
    public void setConcurrencyLimit(int concurrencyLimit) {
        this.concurrencyLimit = concurrencyLimit;
    }
}
