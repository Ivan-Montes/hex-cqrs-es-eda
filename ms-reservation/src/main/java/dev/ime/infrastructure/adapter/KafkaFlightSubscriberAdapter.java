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
public class KafkaFlightSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final BaseProjectorPort flightBaseProjectorPort;
	private final ExtendedProjectorPort flightExtendedProjectorPort;
	
	public KafkaFlightSubscriberAdapter(LoggerUtil loggerUtil, @Qualifier("flightRedisProjectorAdapter")BaseProjectorPort flightBaseProjectorPort,
			@Qualifier("flightRedisProjectorAdapter")ExtendedProjectorPort flightExtendedProjectorPort) {
		super();
		this.loggerUtil = loggerUtil;
		this.flightBaseProjectorPort = flightBaseProjectorPort;
		this.flightExtendedProjectorPort = flightExtendedProjectorPort;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.FLIGHT_CREATED, ApplicationConstant.FLIGHT_UPDATED, ApplicationConstant.FLIGHT_DELETED}, groupId = "msreservation-consumer-flight")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {

		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();
		
		switch ( consumerRecord.topic() ) {
			
			case ApplicationConstant.FLIGHT_CREATED -> flightBaseProjectorPort.create( event );
			case ApplicationConstant.FLIGHT_UPDATED -> flightExtendedProjectorPort.update( event );
			case ApplicationConstant.FLIGHT_DELETED -> flightBaseProjectorPort.deleteById( event );	
			default -> loggerUtil.logInfoAction(this.getClass().getSimpleName(),"onMessage", "Default Response");
	
		}		
	}	
	
}
