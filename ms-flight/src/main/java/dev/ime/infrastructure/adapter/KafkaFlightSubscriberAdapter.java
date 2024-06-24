package dev.ime.infrastructure.adapter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.SubscriberPort;
import dev.ime.domain.port.outbound.FlightProjectorPort;

@Component
public class KafkaFlightSubscriberAdapter implements SubscriberPort{
	
	private final LoggerUtil loggerUtil;
	private final FlightProjectorPort flightProjectorPort;

	public KafkaFlightSubscriberAdapter(LoggerUtil loggerUtil, FlightProjectorPort flightProjectorPort) {
		super();
		this.loggerUtil = loggerUtil;
		this.flightProjectorPort = flightProjectorPort;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.FLIGHT_CREATED, ApplicationConstant.FLIGHT_UPDATED, ApplicationConstant.FLIGHT_DELETED,}, groupId = "msflight-consumer-flight")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {
		
		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();

	switch ( consumerRecord.topic() ) {
		
		case ApplicationConstant.FLIGHT_CREATED -> flightProjectorPort.create( event );
		case ApplicationConstant.FLIGHT_UPDATED -> flightProjectorPort.update( event );
		case ApplicationConstant.FLIGHT_DELETED -> flightProjectorPort.deleteById( event );
		default -> loggerUtil.logInfoAction(this.getClass().getSimpleName(),"onMessage", "Default Response");

		}	
	}
	
}
