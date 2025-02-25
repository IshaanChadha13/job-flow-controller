package com.example.capstone.job_flow_controller.dto;

import com.example.capstone.job_flow_controller.model.Event;
import com.example.capstone.job_flow_controller.model.EventTypes;
import com.example.capstone.job_flow_controller.model.UpdateEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateAlertEvent implements Event<UpdateEvent> {

    private String eventId;
    private UpdateEvent payload;

    // NEW
    private String destinationTopic;

    public UpdateAlertEvent() {}

    public UpdateAlertEvent(String eventId, UpdateEvent payload, String destinationTopic) {
        // if eventId is null => random
        this.eventId = (eventId == null || eventId.isEmpty())
                ? UUID.randomUUID().toString()
                : eventId;
        this.payload = payload;
        this.destinationTopic = destinationTopic;
    }

    @Override
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public EventTypes getType() {
        return EventTypes.UPDATE_FINDING;  // remains UPDATE_FINDING
    }

    @Override
    public UpdateEvent getPayload() {
        return payload;
    }
    public void setPayload(UpdateEvent payload) {
        this.payload = payload;
    }

    public String getDestinationTopic() {
        return destinationTopic;
    }
    public void setDestinationTopic(String destinationTopic) {
        this.destinationTopic = destinationTopic;
    }
}

