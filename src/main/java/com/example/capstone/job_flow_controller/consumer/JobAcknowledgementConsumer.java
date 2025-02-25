package com.example.capstone.job_flow_controller.consumer;

import com.example.capstone.job_flow_controller.dto.ParseAcknowledgement;
import com.example.capstone.job_flow_controller.dto.ScanAcknowledgement;
import com.example.capstone.job_flow_controller.dto.UpdateAcknowledgement;
import com.example.capstone.job_flow_controller.entity.JobEntity;
import com.example.capstone.job_flow_controller.entity.JobStatus;
import com.example.capstone.job_flow_controller.model.AcknowledgementEvent;
import com.example.capstone.job_flow_controller.model.AcknowledgementStatus;
import com.example.capstone.job_flow_controller.repository.JobRepository;
import com.example.capstone.job_flow_controller.service.SchedulingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JobAcknowledgementConsumer {

    private final JobRepository jobRepository;
    private final SchedulingService schedulingService;
    private final ObjectMapper objectMapper;

    public JobAcknowledgementConsumer(JobRepository jobRepository, SchedulingService schedulingService) {
        this.jobRepository = jobRepository;
        this.schedulingService = schedulingService;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "${kafka.topics.job-acknowledgement-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void onJobAck(String message) {
        try {
            AcknowledgementEvent ackEvent = null;

            // 1) Try to deserialize as ScanAcknowledgement
            try {
                ScanAcknowledgement scanAck = objectMapper.readValue(message, ScanAcknowledgement.class);
                ackEvent = scanAck.getPayload();
            } catch (Exception scanEx) {

                // 2) If that fails, try ParseAcknowledgement
                try {
                    ParseAcknowledgement parseAck = objectMapper.readValue(message, ParseAcknowledgement.class);
                    ackEvent = parseAck.getPayload();
                } catch (Exception parseEx) {

                    // 3) Finally, try UpdateAcknowledgement
                    try {
                        UpdateAcknowledgement updateAck = objectMapper.readValue(message, UpdateAcknowledgement.class);
                        ackEvent = updateAck.getPayload();
                    } catch (Exception updateEx) {
                        // If all attempts failed, it's an unknown ack format
                        System.out.println("[JFC] Unknown acknowledgement format => " + message);
                        return;
                    }
                }
            }

            // At this point, ackEvent is populated with jobId & status
            String jobId = ackEvent.getJobId();  // eventId from the original job
            AcknowledgementStatus status = ackEvent.getStatus();  // SUCCESS or FAILURE

            System.out.println("Job acknowledgement received in JFC successfully for: " + jobId);

            // 4) Update the job in DB
            JobEntity job = jobRepository.findByExternalJobId(jobId);
            if (job == null) {
                System.out.println("[JFC] ACK received for unknown jobId=" + jobId);
                return;
            }

            if (status == AcknowledgementStatus.SUCCESS) {
                job.setStatus(JobStatus.SUCCESS);
            } else {
                job.setStatus(JobStatus.FAILURE);
            }
            jobRepository.save(job);

            System.out.printf("[JFC] Job %s updated to %s%n", jobId, job.getStatus());

            // Immediately re-run scheduling to free concurrency slot
            schedulingService.scheduleJobs();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

