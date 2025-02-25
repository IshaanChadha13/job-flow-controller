package com.example.capstone.job_flow_controller.model;

public class ScanMessage {

    private Long tenantId;
    private String tool;

    public ScanMessage(Long tenantId, String tool) {
        this.tenantId = tenantId;
        this.tool = tool;
    }

    public ScanMessage() {

    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

}
