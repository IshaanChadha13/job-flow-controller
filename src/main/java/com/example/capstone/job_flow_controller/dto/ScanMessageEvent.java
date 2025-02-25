package com.example.capstone.job_flow_controller.dto;

import com.example.capstone.job_flow_controller.model.Event;
import com.example.capstone.job_flow_controller.model.EventTypes;
import com.example.capstone.job_flow_controller.model.ScanMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanMessageEvent implements Event<ScanMessage> {

    private String eventId;
    private ScanMessage payload;

    // NEW: which topic JFC should eventually send this job to
    private String destinationTopic;

    public ScanMessageEvent(ScanMessage payload, String eventId, String destinationTopic) {
        this.payload = payload;
        this.eventId = eventId;
        this.destinationTopic = destinationTopic;
    }

    public ScanMessageEvent() {

    }


    public void setPayload(ScanMessage payload) {
        this.payload = payload;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getDestinationTopic() {
        return destinationTopic;
    }

    public void setDestinationTopic(String destinationTopic) {
        this.destinationTopic = destinationTopic;
    }

    @Override
    public EventTypes getType() {
        return EventTypes.SCAN_PULL;
    }

    @Override
    public ScanMessage getPayload() {
        return payload;
    }
}
