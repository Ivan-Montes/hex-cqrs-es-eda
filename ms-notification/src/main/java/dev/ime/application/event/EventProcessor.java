package dev.ime.application.event;

import java.lang.reflect.Method;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.config.LoggerUtil;
import dev.ime.db.DatabaseSequenceAdapter;
import dev.ime.kafka.PublisherPort;

@Component
public class EventProcessor {

	private final LoggerUtil loggerUtil;
	private final PublisherPort publisherPort;	
	private final DatabaseSequenceAdapter databaseSequenceAdapter;
	
	public EventProcessor(LoggerUtil loggerUtil, PublisherPort publisherPort,
			DatabaseSequenceAdapter databaseSequenceAdapter) {
		super();
		this.loggerUtil = loggerUtil;
		this.publisherPort = publisherPort;
		this.databaseSequenceAdapter = databaseSequenceAdapter;
	}	
	
	public void processEvent(Event event) {
		
		try {
	        Method method = getClass().getMethod("processEvent", event.getClass());
	        method.invoke(this, event);
		} catch (Exception e) {
			logInfo(event);
		}
	}
	
	public void processEvent(PaymentCreatedEvent event) {
		
		logInfo(event);
		NotificationCreatedEvent notificationEvent = new NotificationCreatedEvent(
				databaseSequenceAdapter.generateSequence(),
				UUID.randomUUID(),
				event.getPaymentId()
				);
		logInfo(notificationEvent);
		publisherPort.publishEvent(notificationEvent);
		
	}

	public void processEvent(PaymentUpdatedEvent event) {

		logInfo(event);
		NotificationUpdatedEvent notificationEvent = new NotificationUpdatedEvent(
				databaseSequenceAdapter.generateSequence(),
				UUID.randomUUID(),
				event.getPaymentId()
				);
		logInfo(notificationEvent);
		publisherPort.publishEvent(notificationEvent);
		
	}

	public void processEvent(PaymentDeletedEvent event) {

		logInfo(event);
		NotificationDeletedEvent notificationEvent = new NotificationDeletedEvent(
				databaseSequenceAdapter.generateSequence(),
				UUID.randomUUID()
				);
		logInfo(notificationEvent);
		publisherPort.publishEvent(notificationEvent);
		
	}

	private void logInfo(Event event) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), event.getClass().getSimpleName(), event.toString());	   
	    
	}

}
