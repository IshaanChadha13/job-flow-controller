package com.example.capstone.job_flow_controller.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.scan-request-topic}")
    private String scanRequestTopic;

    @Value("${kafka.topics.scan-pull-topic}")
    private String scanPullTopic;

    @Value("${kafka.topics.scan-parse-topic}")
    private String scanParseTopic;

    @Value("${kafka.topics.jfc-parser-topic}")
    private String jfcParserTopic;

    @Value("${kafka.topics.update-state-topic}")
    private String updateStateTopic;

    @Value("${kafka.topics.jfc-bg-job-topic}")
    private String jfcBgJobTopic;

    @Value("${kafka.topics.jfc-jobs}")
    private String jfcJobs;

    @Value("${kafka.topics.job-acknowledgement-topic}")
    private String jobAckTopic;

    @Value("${kafka.topics.partition-count}")
    private int partitionCount;

    @Value("${kafka.topics.replication-factor}")
    private short replicationFactor;

    @Bean
    public NewTopic scanRequestTopic() {
        return TopicBuilder.name(scanRequestTopic)
                .partitions(partitionCount)
                .replicas(replicationFactor)
                .build();
    }

    @Bean
    public NewTopic scanPullTopic() {
        return TopicBuilder.name(scanPullTopic)
                .partitions(partitionCount)
                .replicas(replicationFactor)
                .build();
    }

    @Bean
    public NewTopic scanParseTopic() {
        return TopicBuilder.name(scanParseTopic)
                .partitions(partitionCount)
                .replicas(replicationFactor)
                .build();
    }

    @Bean
    public NewTopic jobAckTopic() {
        return TopicBuilder.name(jobAckTopic)
                .partitions(partitionCount)
                .replicas(replicationFactor)
                .build();
    }
    @Bean
    public NewTopic jfcParserTopic() {
        return TopicBuilder.name(jfcParserTopic)
                .partitions(partitionCount)
                .replicas(replicationFactor)
                .build();
    }

    @Bean
    public NewTopic updateStateTopic() {
        return TopicBuilder.name(updateStateTopic)
                .partitions(partitionCount)
                .replicas(replicationFactor)
                .build();
    }

    @Bean
    public NewTopic jfcBgJobTopic() {
        return TopicBuilder.name(jfcBgJobTopic)
                .partitions(partitionCount)
                .replicas(replicationFactor)
                .build();
    }

    @Bean
    public NewTopic jfcJobs() {
        return TopicBuilder.name(jfcJobs)
                .partitions(partitionCount)
                .replicas(replicationFactor)
                .build();
    }
}
