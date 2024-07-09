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
import dev.ime.domain.port.outbound.ExtendedProjectorPort;

@Component
public class KafkaSeatSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final BaseProjectorPort seatBaseProjectorPort;
	private final ExtendedProjectorPort seatExtendedProjectorPort;
	
	public KafkaSeatSubscriberAdapter(LoggerUtil loggerUtil, @Qualifier("seatRedisProjectorAdapter")BaseProjectorPort seatBaseProjectorPort,
			@Qualifier("seatRedisProjectorAdapter")ExtendedProjectorPort seatExtendedProjectorPort) {
		super();
		this.loggerUtil = loggerUtil;
		this.seatBaseProjectorPort = seatBaseProjectorPort;
		this.seatExtendedProjectorPort = seatExtendedProjectorPort;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.SEAT_CREATED, ApplicationConstant.SEAT_UPDATED, ApplicationConstant.SEAT_DELETED},	groupId = "msreservation-consumer-seat")	
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {
		
		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();
		
		switch ( consumerRecord.topic() ) {
			
			case ApplicationConstant.SEAT_CREATED -> seatBaseProjectorPort.create( event );
			case ApplicationConstant.SEAT_UPDATED -> seatExtendedProjectorPort.update( event );
			case ApplicationConstant.SEAT_DELETED -> seatBaseProjectorPort.deleteById( event );		
			default -> loggerUtil.logInfoAction(this.getClass().getSimpleName(),"onMessage", "Default Response");
	
		}		
	}

}
