package com.example.capstone.job_flow_controller.dto;

import com.example.capstone.job_flow_controller.model.Acknowledgement;
import com.example.capstone.job_flow_controller.model.AcknowledgementEvent;

import java.util.UUID;

public class UpdateAcknowledgement implements Acknowledgement<AcknowledgementEvent> {

    private String acknowledgementId;
    private AcknowledgementEvent payload;

    public UpdateAcknowledgement() {}
    public UpdateAcknowledgement(String acknowledgementId, AcknowledgementEvent payload) {
        this.acknowledgementId = (acknowledgementId == null || acknowledgementId.isEmpty())
                ? UUID.randomUUID().toString() : acknowledgementId;
        this.payload = payload;
    }
    public void setAcknowledgementId(String acknowledgementId) {
        this.acknowledgementId = acknowledgementId;
    }
    public void setPayload(AcknowledgementEvent payload) {
        this.payload = payload;
    }
    @Override
    public String getAcknowledgementId() {
        return acknowledgementId;
    }
    @Override
    public AcknowledgementEvent getPayload() {
        return payload;
    }
}
