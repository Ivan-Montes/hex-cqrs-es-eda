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
public class KafkaSeatSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	@Qualifier("seatProjectorAdapter")
	private final GenericProjectorPort seatProjectorAdapter;

	public KafkaSeatSubscriberAdapter(LoggerUtil loggerUtil, GenericProjectorPort seatProjectorAdapter) {
		super();
		this.loggerUtil = loggerUtil;
		this.seatProjectorAdapter = seatProjectorAdapter;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.SEAT_CREATED, ApplicationConstant.SEAT_UPDATED, ApplicationConstant.SEAT_DELETED}, groupId = "msseat-consumer-seat")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {

		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();

		switch ( consumerRecord.topic() ) {
			
			case ApplicationConstant.SEAT_CREATED -> seatProjectorAdapter.create( event );
			case ApplicationConstant.SEAT_UPDATED -> seatProjectorAdapter.update( event );
			case ApplicationConstant.SEAT_DELETED -> seatProjectorAdapter.deleteById( event );			
			default -> loggerUtil.logInfoAction(this.getClass().getSimpleName(),"onMessage", "Default Response");
	
		}		
	}
	
}
