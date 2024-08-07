package dev.ime.infrastructure.adapter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.SubscriberPort;
import dev.ime.domain.port.outbound.BaseProjectorPort;

@Component
public class KafkaRegistrySubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final BaseProjectorPort registryBaseProjectorPort;
	
	public KafkaRegistrySubscriberAdapter(LoggerUtil loggerUtil, BaseProjectorPort registryBaseProjectorPort) {
		super();
		this.loggerUtil = loggerUtil;
		this.registryBaseProjectorPort = registryBaseProjectorPort;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.REGISTRY_CREATED}, groupId = "msregistry-consumer-registry")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {
		
		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();
		registryBaseProjectorPort.create(event);
		
	}
	
}
