package dev.ime.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ime.application.event.Event;
import dev.ime.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;
import dev.ime.visitor.ManageEventVisitor;

@Component
public class KafkaReservationSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final ManageEventVisitor manageEventVisitor;


	public KafkaReservationSubscriberAdapter(LoggerUtil loggerUtil, ManageEventVisitor manageEventVisitor) {
		super();
		this.loggerUtil = loggerUtil;
		this.manageEventVisitor = manageEventVisitor;
	}


	@Override
	@KafkaListener(topics = {ApplicationConstant.RESERVATION_CREATED, ApplicationConstant.RESERVATION_UPDATED, ApplicationConstant.RESERVATION_DELETED}, groupId = "mspayment-consumer-reservation")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {

		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();	
		event.accept(manageEventVisitor);		
		
	}	
	
}
