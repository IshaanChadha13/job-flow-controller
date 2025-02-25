package com.example.capstone.job_flow_controller.consumer;

import com.example.capstone.job_flow_controller.dto.*;
import com.example.capstone.job_flow_controller.entity.JobEntity;
import com.example.capstone.job_flow_controller.entity.JobStatus;
import com.example.capstone.job_flow_controller.model.*;
import com.example.capstone.job_flow_controller.repository.JobRepository;
import com.example.capstone.job_flow_controller.service.JobFlowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class JfcJobsConsumer {

    private final JobRepository jobRepository;
    private final JobFlowService jobFlowService;
    private final ObjectMapper objectMapper;

    public JfcJobsConsumer(JobRepository jobRepository, JobFlowService jobFlowService) {
        this.jobRepository = jobRepository;
        this.jobFlowService = jobFlowService;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Single consumer for the "jfc-jobs" topic.
     * We detect the event "type" field to see if it's SCAN_PULL, SCAN_PARSE, or UPDATE_FINDING,
     * then parse the appropriate DTO and store in the DB with a recognized JobCategory.
     */
    @KafkaListener(topics = "${kafka.topics.jfc-jobs}", groupId = "${spring.kafka.consumer.group-id}")
    public void onJfcJobMessage(String message) {
        try {
            // 1) Peek at "type" to see which event type we have
            EventTypes eventType = detectEventType(message);
            switch (eventType) {
                case SCAN_PULL -> handleScanPull(message);
                case SCAN_PARSE -> handleScanParse(message);
                case UPDATE_FINDING -> handleUpdateFinding(message);
                case CREATE_TICKET -> handleCreateTicket(message);
                case TRANSITION_TICKET -> handleTransitionTicket(message);
                default -> {
                    System.out.println("[JFC] Unknown event type => " + eventType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Simple method to read the "type" field from JSON
     */
    private EventTypes detectEventType(String rawJson) throws Exception {
        String typeStr = objectMapper.readTree(rawJson).path("type").asText();
        return EventTypes.valueOf(typeStr.toUpperCase());
    }

    // ------------------------------------------------------------------
    // SCAN_PULL
    // ------------------------------------------------------------------
    private void handleScanPull(String message) throws Exception {
        // parse as ScanMessageEvent
        ScanMessageEvent event = objectMapper.readValue(message, ScanMessageEvent.class);

        String eventId = event.getEventId();
        ScanMessage payload = event.getPayload();
        Long tenantId = payload.getTenantId();
        String tool = payload.getTool();

        // e.g. SCAN_PULL_CODESCAN, SCAN_PULL_DEPENDABOT, etc.
        JobCategory category = jobFlowService.determinePullJobCategory(tool);

        JobEntity jobEntity = new JobEntity(eventId, category, tenantId, message);
        jobEntity.setStatus(JobStatus.NEW);

        // store the destinationTopic from the event
        jobEntity.setDestinationTopic(event.getDestinationTopic());

        jobRepository.save(jobEntity);

        System.out.printf("[JFC] Received SCAN_PULL => eventId=%s, category=%s, destTopic=%s, status=NEW%n",
                eventId, category, event.getDestinationTopic());
    }

    // ------------------------------------------------------------------
    // SCAN_PARSE
    // ------------------------------------------------------------------
    private void handleScanParse(String message) throws Exception {
        ParserMessageEvent event = objectMapper.readValue(message, ParserMessageEvent.class);

        String eventId = event.getEventId();
        ParserMessage payload = event.getPayload();
        Long tenantId = payload.getTenantId();
        String toolType = payload.getToolType();

        // SCAN_PARSE_CODESCAN, etc.
        JobCategory parseCategory = jobFlowService.determineParseJobCategory(toolType);

        JobEntity jobEntity = new JobEntity(eventId, parseCategory, tenantId, message);
        jobEntity.setStatus(JobStatus.NEW);
        jobEntity.setDestinationTopic(event.getDestinationTopic());

        jobRepository.save(jobEntity);

        System.out.printf("[JFC] Received SCAN_PARSE => eventId=%s, category=%s, tenantId=%d, destTopic=%s%n",
                eventId, parseCategory, tenantId, event.getDestinationTopic());
    }

    // ------------------------------------------------------------------
    // UPDATE_FINDING
    // ------------------------------------------------------------------
    private void handleUpdateFinding(String message) throws Exception {
        UpdateAlertEvent event = objectMapper.readValue(message, UpdateAlertEvent.class);

        String eventId = event.getEventId();
        // tenantId is stored in event.getPayload().getTenantId()
        Long tenantId = Long.valueOf(event.getPayload().getTenantId());

        JobEntity jobEntity = new JobEntity(
                eventId,
                JobCategory.UPDATE_FINDING,
                tenantId,
                message
        );
        jobEntity.setStatus(JobStatus.NEW);
        jobEntity.setDestinationTopic(event.getDestinationTopic());

        jobRepository.save(jobEntity);

        System.out.printf("[JFC] Received UPDATE_FINDING => eventId=%s, tenantId=%d, destTopic=%s%n",
                eventId, tenantId, event.getDestinationTopic());
    }

    private void handleCreateTicket(String message) throws Exception {

        System.out.println("Hello 1");
        CreateTicketRequestEvent event = objectMapper.readValue(message, CreateTicketRequestEvent.class);

        String eventId = event.getEventId();

        JobEntity jobEntity = new JobEntity(
                eventId,
                JobCategory.CREATE_TICKET,
                event.getPayload().getTenantId(),
                message
        );

        jobEntity.setStatus(JobStatus.NEW);
        jobEntity.setDestinationTopic(event.getDestinationTopic());

        System.out.println("Hello 2");

        jobRepository.save(jobEntity);

        System.out.printf("[JFC] Received TICKETING_CREATE => eventId=%s, tenantId=%d, destTopic=%s%n",
                eventId, event.getPayload().getTenantId(), event.getDestinationTopic());
    }

    private void handleTransitionTicket(String message) throws Exception {
        TransitionTicketRequestEvent event = objectMapper.readValue(message, TransitionTicketRequestEvent.class);

        String eventId = event.getEventId();

        JobEntity jobEntity = new JobEntity(
                eventId,
                JobCategory.TRANSITION_TICKET,
                event.getPayload().getTenantId(),
                message
        );

        jobEntity.setStatus(JobStatus.NEW);
        jobEntity.setDestinationTopic(event.getDestinationTopic());

        jobRepository.save(jobEntity);

        System.out.printf("[JFC] Received TICKETING_TRANSITION => eventId=%s, tenantId=%d, destTopic=%s%n",
                eventId, event.getPayload().getTenantId(), event.getDestinationTopic());
    }
}
