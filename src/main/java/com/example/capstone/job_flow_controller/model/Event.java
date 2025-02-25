package com.example.capstone.job_flow_controller.model;

public interface Event<T> {

    EventTypes getType();
    T getPayload();
    String getEventId();
}
