package dev.ime.infrastructure.adapter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.SubscriberPort;
import dev.ime.domain.port.outbound.PlaneRedisProjectorPort;

@Component
public class KafkaPlaneSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final PlaneRedisProjectorPort planeRedisProjectorPort;
	
	public KafkaPlaneSubscriberAdapter(LoggerUtil loggerUtil, PlaneRedisProjectorPort planeRedisProjectorPort) {
		super();
		this.loggerUtil = loggerUtil;
		this.planeRedisProjectorPort = planeRedisProjectorPort;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.PLANE_CREATED, ApplicationConstant.PLANE_DELETED}, groupId = "msflight-consumer-plane")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {

		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();

	switch ( consumerRecord.topic() ) {
		
		case ApplicationConstant.PLANE_CREATED -> planeRedisProjectorPort.save( event );
		case ApplicationConstant.PLANE_DELETED -> planeRedisProjectorPort.deleteById( event );
		default -> loggerUtil.logInfoAction(this.getClass().getSimpleName(),"onMessage", "Default Response");

		}		
	}

}
