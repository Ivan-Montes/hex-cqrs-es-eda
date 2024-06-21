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
	            TopicBuilder.name(ApplicationConstant.FLIGHT_CREATED).build(),
	            TopicBuilder.name(ApplicationConstant.FLIGHT_UPDATED).build(),
	            TopicBuilder.name(ApplicationConstant.FLIGHT_DELETED).build()
	            );
	}
	
}
