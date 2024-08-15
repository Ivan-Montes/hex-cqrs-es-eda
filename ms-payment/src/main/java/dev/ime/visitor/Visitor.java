package dev.ime.visitor;

import dev.ime.application.event.PaymentCreatedEvent;
import dev.ime.application.event.PaymentDeletedEvent;
import dev.ime.application.event.PaymentUpdatedEvent;
import dev.ime.application.event.ReservationCreatedEvent;
import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.application.event.ReservationUpdatedEvent;

public interface Visitor {

	void visit(ReservationCreatedEvent event);
	void visit(ReservationUpdatedEvent event);
	void visit(ReservationDeletedEvent event);
	void visit(PaymentCreatedEvent event);
	void visit(PaymentUpdatedEvent event);
	void visit(PaymentDeletedEvent event);
	
}
