package dev.ime.infrastructure.adapter;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.GenericCommandServicePort;
import dev.ime.domain.port.inbound.SubscriberPort;

@Component
public class KafkaClientSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final GenericCommandServicePort<Event> genericCommandServicePort;
	
	public KafkaClientSubscriberAdapter(LoggerUtil loggerUtil,
			GenericCommandServicePort<Event> genericCommandServicePort) {
		super();
		this.loggerUtil = loggerUtil;
		this.genericCommandServicePort = genericCommandServicePort;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.CLIENT_CREATED,ApplicationConstant.CLIENT_DELETED}, groupId = "msregistry-consumer-client")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {
		
		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();
		genericCommandServicePort.create(event);			
		
	}
	
	
}
