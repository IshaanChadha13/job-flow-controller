package com.example.capstone.job_flow_controller.producer;

import com.example.capstone.job_flow_controller.dto.UpdateAlertEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public JobProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void sendToAnyTopic(Object eventObject, String topicName) {
        try {
            String json = objectMapper.writeValueAsString(eventObject);
            kafkaTemplate.send(topicName, json);
            System.out.printf("[JobProducer] Sent event to topic=%s => %s%n", topicName, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}