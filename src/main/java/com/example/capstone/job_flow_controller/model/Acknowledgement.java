package com.example.capstone.job_flow_controller.model;

public interface Acknowledgement<T> {

    String getAcknowledgementId();
    T getPayload();
}
