package dev.ime.infrastructure.adapter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.SubscriberPort;
import dev.ime.domain.port.outbound.BaseProjectorPort;

@Component
public class KafkaClientSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final BaseProjectorPort clientBaseProjectorPort;
	
	public KafkaClientSubscriberAdapter(LoggerUtil loggerUtil, @Qualifier("clientRedisProjectorAdapter")BaseProjectorPort clientBaseProjectorPort) {
		super();
		this.loggerUtil = loggerUtil;
		this.clientBaseProjectorPort = clientBaseProjectorPort;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.CLIENT_CREATED,ApplicationConstant.CLIENT_DELETED}, groupId = "msreservation-consumer-client")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {
		
		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();
		
		switch ( consumerRecord.topic() ) {
			
			case ApplicationConstant.CLIENT_CREATED -> clientBaseProjectorPort.create( event );
			case ApplicationConstant.CLIENT_DELETED -> clientBaseProjectorPort.deleteById( event );					
			default -> loggerUtil.logInfoAction(this.getClass().getSimpleName(),"onMessage", "Default Response");
			
		}
	}
	
	
}
