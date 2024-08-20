package dev.ime.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import dev.ime.application.event.Event;
import dev.ime.application.event.EventProcessor;
import dev.ime.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;

@Component
public class KafkaPaymentSubscriberAdapter implements SubscriberPort{

	private final LoggerUtil loggerUtil;
	private final EventProcessor eventProcessor;

	public KafkaPaymentSubscriberAdapter(LoggerUtil loggerUtil, EventProcessor eventProcessor) {
		super();
		this.loggerUtil = loggerUtil;
		this.eventProcessor = eventProcessor;
	}

	@Override
	@KafkaListener(topics = {ApplicationConstant.PAYMENT_CREATED, ApplicationConstant.PAYMENT_UPDATED, ApplicationConstant.PAYMENT_DELETED}, groupId = "msnotification-consumer-payment")
	public void onMessage(ConsumerRecord<String, Object> consumerRecord) {

		loggerUtil.logInfoAction(this.getClass().getSimpleName(), "onMessage", "received");
		Event event = (Event) consumerRecord.value();	
		eventProcessor.processEvent(event);	
		
	}	
	
}
