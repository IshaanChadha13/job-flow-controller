package com.example.capstone.job_flow_controller.service;

import com.example.capstone.job_flow_controller.dto.*;
import com.example.capstone.job_flow_controller.entity.JobEntity;
import com.example.capstone.job_flow_controller.entity.JobStatus;
import com.example.capstone.job_flow_controller.producer.JobProducer;
import com.example.capstone.job_flow_controller.repository.JobRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.example.capstone.job_flow_controller.model.EventTypes;
import com.example.capstone.job_flow_controller.model.JobCategory;

@Service
public class SchedulingService {

    private final JobRepository jobRepository;
    private final ConcurrencyDbService concurrencyDbService;
    private final JobProducer jobProducer;
    private final ObjectMapper objectMapper;

    public SchedulingService(
            JobRepository jobRepository,
            ConcurrencyDbService concurrencyDbService,
            JobProducer jobProducer
    ) {
        this.jobRepository = jobRepository;
        this.concurrencyDbService = concurrencyDbService;
        this.jobProducer = jobProducer;
        this.objectMapper = new ObjectMapper();
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public synchronized void scheduleJobs() {
        for (JobCategory category : JobCategory.values()) {
            // get the concurrency limit for the string name of the category
            int limitForCategory = concurrencyDbService.getJobLimit(category.name());

            int currentlyInProgress = jobRepository.countByJobCategoryAndStatus(category, JobStatus.IN_PROGRESS);
            int availableSlots = limitForCategory - currentlyInProgress;
            if (availableSlots <= 0) {
                continue;
            }

            // find all NEW jobs for that category
            List<JobEntity> newJobs = jobRepository.findByJobCategoryAndStatus(category, JobStatus.NEW);

            for (JobEntity job : newJobs) {
                if (availableSlots <= 0) break;

                // check tenant concurrency
                int tenantLimit = concurrencyDbService.getTenantLimit(job.getTenantId());
                int tenantInProgress = jobRepository.countByJobCategoryAndTenantIdAndStatus(
                        category, job.getTenantId(), JobStatus.IN_PROGRESS
                );
                if (tenantInProgress >= tenantLimit) {
                    continue;
                }

                // Mark job READY
                job.setStatus(JobStatus.READY);
                jobRepository.save(job);

                // publish
                publishJob(job);

                // Mark IN_PROGRESS
                job.setStatus(JobStatus.IN_PROGRESS);
                jobRepository.save(job);

                availableSlots--;
            }
        }
    }

    private void publishJob(JobEntity job) {
        try {
            String rawJson = job.getPayload();
            String destinationTopic = job.getDestinationTopic();

            EventTypes eventType = detectEventType(rawJson);

            // parse the original event
            Object eventObject;
            switch (eventType) {
                case SCAN_PULL -> eventObject = objectMapper.readValue(rawJson, ScanMessageEvent.class);
                case SCAN_PARSE -> eventObject = objectMapper.readValue(rawJson, ParserMessageEvent.class);
                case UPDATE_FINDING -> eventObject = objectMapper.readValue(rawJson, UpdateAlertEvent.class);
                case CREATE_TICKET -> eventObject = objectMapper.readValue(rawJson, CreateTicketRequestEvent.class);
                case TRANSITION_TICKET -> eventObject = objectMapper.readValue(rawJson, TransitionTicketRequestEvent.class);
                case NEW_SCAN -> eventObject = objectMapper.readValue(rawJson, NewScanRunbookEvent.class);
                default -> {
                    System.out.println("[SchedulingService] Unknown eventType => " + eventType);
                    return;
                }
            }

            // produce
            jobProducer.sendToAnyTopic(eventObject, destinationTopic);

            System.out.printf("[SchedulingService] Published jobId=%s => topic=%s%n",
                    job.getExternalJobId(), destinationTopic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EventTypes detectEventType(String rawJson) throws Exception {
        JsonNode node = objectMapper.readTree(rawJson);
        String typeStr = node.path("type").asText("");
        return EventTypes.valueOf(typeStr.toUpperCase());
    }
}