package dev.ime.visitor;

import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.event.Event;
import dev.ime.application.event.PaymentCreatedEvent;
import dev.ime.application.event.PaymentDeletedEvent;
import dev.ime.application.event.PaymentUpdatedEvent;
import dev.ime.application.event.ReservationCreatedEvent;
import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.application.event.ReservationUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.db.DatabaseSequenceAdapter;
import dev.ime.kafka.PublisherPort;

@Component
public class ManageEventVisitor implements Visitor{

	private final LoggerUtil loggerUtil;
	private final PublisherPort publisherPort;	
	private final DatabaseSequenceAdapter databaseSequenceAdapter;	

	public ManageEventVisitor(LoggerUtil loggerUtil, PublisherPort publisherPort,
			DatabaseSequenceAdapter databaseSequenceAdapter) {
		super();
		this.loggerUtil = loggerUtil;
		this.publisherPort = publisherPort;
		this.databaseSequenceAdapter = databaseSequenceAdapter;
	}

	@Override
	public void visit(ReservationCreatedEvent event) {
		
		logInfo(event);
		PaymentCreatedEvent paymentEvent = new PaymentCreatedEvent(
				databaseSequenceAdapter.generateSequence(),
				UUID.randomUUID(),
				event.getReservationId(),
				event.getClientId(),
				event.getFlightId()
				);
		logInfo(paymentEvent);
		publisherPort.publishEvent(paymentEvent);
	}

	@Override
	public void visit(ReservationUpdatedEvent event) {
		
		logInfo(event);
		PaymentUpdatedEvent paymentEvent = new PaymentUpdatedEvent(
				databaseSequenceAdapter.generateSequence(),
				UUID.randomUUID(),
				event.getReservationId(),
				event.getClientId(),
				event.getFlightId()
				);
		logInfo(paymentEvent);
		publisherPort.publishEvent(paymentEvent);
	}

	@Override
	public void visit(ReservationDeletedEvent event) {
		
		logInfo(event);
		PaymentDeletedEvent paymentEvent = new PaymentDeletedEvent(
				databaseSequenceAdapter.generateSequence(),
				UUID.randomUUID()
				);
		logInfo(paymentEvent);
		publisherPort.publishEvent(paymentEvent);
	}

	@Override
	public void visit(PaymentCreatedEvent event) {
		
		logInfo(event);
	}
	
	@Override
	public void visit(PaymentUpdatedEvent event) {

		logInfo(event);
		
	}

	@Override
	public void visit(PaymentDeletedEvent event) {

		logInfo(event);
		
	}

	private void logInfo(Event event) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), event.getClass().getSimpleName(), event.toString());	   
	    
	}

}
