package dev.ime.infrastructure.adapter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.SubscriberPort;
import dev.ime.domain.port.outbound.GenericProjectorPort;

@Component
public class KafkaPlaneSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	@Qualifier("planeProjectorAdapter")
	private final GenericProjectorPort planeProjectorAdapter;
	
	public KafkaPlaneSubscriberAdapter(LoggerUtil loggerUtil, GenericProjectorPort planeProjectorAdapter) {
		super();
		this.loggerUtil = loggerUtil;
		this.planeProjectorAdapter = planeProjectorAdapter;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.PLANE_CREATED, ApplicationConstant.PLANE_UPDATED, ApplicationConstant.PLANE_DELETED}, groupId = "msseat-consumer-plane")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {

		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();

		switch ( consumerRecord.topic() ) {

			case ApplicationConstant.PLANE_CREATED -> planeProjectorAdapter.create( event );
			case ApplicationConstant.PLANE_UPDATED -> planeProjectorAdapter.update( event );
			case ApplicationConstant.PLANE_DELETED -> planeProjectorAdapter.deleteById( event );			
			default -> loggerUtil.logInfoAction(this.getClass().getSimpleName(),"onMessage", "Default Response");
	
		}		
	}
	
}
