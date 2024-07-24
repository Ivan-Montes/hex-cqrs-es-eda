package dev.ime.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

import dev.ime.application.config.ApplicationConstant;

@Configuration
public class KafkaConfig {
	
	@Bean
	KafkaAdmin.NewTopics topics() {
		
	    return new NewTopics(
	            TopicBuilder.name(ApplicationConstant.SEAT_CREATED).build(),
	            TopicBuilder.name(ApplicationConstant.SEAT_UPDATED).build(),
	            TopicBuilder.name(ApplicationConstant.SEAT_DELETED).build(),
	            TopicBuilder.name(ApplicationConstant.PLANE_CREATED).build(),
	            TopicBuilder.name(ApplicationConstant.PLANE_UPDATED).build(),
	            TopicBuilder.name(ApplicationConstant.PLANE_DELETED).build()
	            );
	}
	
}
