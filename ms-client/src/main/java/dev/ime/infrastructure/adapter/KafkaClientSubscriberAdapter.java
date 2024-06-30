package dev.ime.infrastructure.adapter;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.SubscriberPort;
import dev.ime.domain.port.outbound.ClientProjectorPort;
import dev.ime.application.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;

@Component
public class KafkaClientSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final ClientProjectorPort clientProjectorPort;
	
	public KafkaClientSubscriberAdapter(LoggerUtil loggerUtil, ClientProjectorPort clientProjectorPort) {
		super();
		this.loggerUtil = loggerUtil;
		this.clientProjectorPort = clientProjectorPort;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.CLIENT_CREATED, ApplicationConstant.CLIENT_UPDATED, ApplicationConstant.CLIENT_DELETED}, groupId = "msclient-consumer-client")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {
		
		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();

		switch ( consumerRecord.topic() ) {
		
		case ApplicationConstant.CLIENT_CREATED -> clientProjectorPort.create( event );
		case ApplicationConstant.CLIENT_UPDATED -> clientProjectorPort.update( event );
		case ApplicationConstant.CLIENT_DELETED -> clientProjectorPort.deleteById( event );
		default -> loggerUtil.logInfoAction(this.getClass().getSimpleName(),"onMessage", "Default Response");

		}		
	}

}
