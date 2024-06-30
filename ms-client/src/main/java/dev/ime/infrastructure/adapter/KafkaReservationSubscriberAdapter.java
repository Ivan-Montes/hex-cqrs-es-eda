package dev.ime.infrastructure.adapter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.SubscriberPort;
import dev.ime.domain.port.outbound.ReservationRedisProjectorPort;

@Component
public class KafkaReservationSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final ReservationRedisProjectorPort reservationRedisProjectorPort;	
	
	public KafkaReservationSubscriberAdapter(LoggerUtil loggerUtil,
			ReservationRedisProjectorPort reservationRedisProjectorPort) {
		super();
		this.loggerUtil = loggerUtil;
		this.reservationRedisProjectorPort = reservationRedisProjectorPort;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.RESERVATION_CREATED, ApplicationConstant.RESERVATION_UPDATED, ApplicationConstant.RESERVATION_DELETED}, groupId = "msclient-consumer-reservation")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {

		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();

		switch ( consumerRecord.topic() ) {
		
		case ApplicationConstant.RESERVATION_CREATED -> reservationRedisProjectorPort.create( event );
		case ApplicationConstant.RESERVATION_UPDATED -> reservationRedisProjectorPort.update( event );
		case ApplicationConstant.RESERVATION_DELETED -> reservationRedisProjectorPort.deleteById( event );
		default -> loggerUtil.logInfoAction(this.getClass().getSimpleName(),"onMessage", "Default Response");

		}			
	}	
	
}
