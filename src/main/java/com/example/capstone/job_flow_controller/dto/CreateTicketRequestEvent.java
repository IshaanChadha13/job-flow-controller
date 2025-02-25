package com.example.capstone.job_flow_controller.dto;

import com.example.capstone.job_flow_controller.model.CreateTicketRequestPayload;
import com.example.capstone.job_flow_controller.model.Event;
import com.example.capstone.job_flow_controller.model.EventTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTicketRequestEvent implements Event<CreateTicketRequestPayload> {

    private CreateTicketRequestPayload payload;
    private String eventId;
    private String destinationTopic;

    public CreateTicketRequestEvent() {

    }

    public CreateTicketRequestEvent(CreateTicketRequestPayload payload, String eventId, String destinationTopic) {
        this.payload = payload;
        this.eventId = eventId;
        this.destinationTopic = destinationTopic;
    }

    @Override
    public EventTypes getType() {
        return EventTypes.CREATE_TICKET;
    }

    @Override
    public CreateTicketRequestPayload getPayload() {
        return payload;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    public String getDestinationTopic() {
        return destinationTopic;
    }
}
