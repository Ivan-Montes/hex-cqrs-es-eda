package dev.ime.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;

import dev.ime.config.ApplicationConstant;

@Configuration
public class KafkaConfig {
	
	@Bean
	KafkaAdmin.NewTopics topics() {
		
	    return new NewTopics(
	            TopicBuilder.name(ApplicationConstant.PAYMENT_CREATED).build(),
	            TopicBuilder.name(ApplicationConstant.PAYMENT_UPDATED).build(),
	            TopicBuilder.name(ApplicationConstant.PAYMENT_DELETED).build()
	            );
	}
	
}
