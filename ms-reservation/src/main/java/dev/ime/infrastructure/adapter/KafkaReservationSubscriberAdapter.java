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
public class KafkaReservationSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final BaseProjectorPort reservationBaseProjectorPort;	
	private final ExtendedProjectorPort reservationExtendedProjectorPort;	
	
	public KafkaReservationSubscriberAdapter(LoggerUtil loggerUtil, @Qualifier("reservationProjectorAdapter")BaseProjectorPort reservationBaseProjectorPort,
			@Qualifier("reservationProjectorAdapter")ExtendedProjectorPort reservationExtendedProjectorPort) {
		super();
		this.loggerUtil = loggerUtil;
		this.reservationBaseProjectorPort = reservationBaseProjectorPort;
		this.reservationExtendedProjectorPort = reservationExtendedProjectorPort;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.RESERVATION_CREATED, ApplicationConstant.RESERVATION_UPDATED, ApplicationConstant.RESERVATION_DELETED}, groupId = "msreservation-consumer-reservation")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {
		
		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();
		
		switch ( consumerRecord.topic() ) {
			
			case ApplicationConstant.RESERVATION_CREATED -> reservationBaseProjectorPort.create( event );
			case ApplicationConstant.RESERVATION_UPDATED -> reservationExtendedProjectorPort.update( event );
			case ApplicationConstant.RESERVATION_DELETED -> reservationBaseProjectorPort.deleteById( event );					
			default -> loggerUtil.logInfoAction(this.getClass().getSimpleName(),"onMessage", "Default Response");
	
		}	
		
	}

}
