package dev.ime.visitor;

import static org.mockito.Mockito.times;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class ManageEventVisitorTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private PublisherPort publisherPort;
	
	@Mock
	private DatabaseSequenceAdapter databaseSequenceAdapter;	

	@InjectMocks
	private ManageEventVisitor manageEventVisitor;

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet = new HashSet<>();
	private final Long databaseSequence = 37L;
	private final UUID paymentId = UUID.randomUUID();
	
	@Test
	void ManageEventVisitor_visit_ReservationCreatedEvent_ReturnVoid() {
		
		ReservationCreatedEvent event = new ReservationCreatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.when(databaseSequenceAdapter.generateSequence()).thenReturn(7L);
		Mockito.doNothing().when(publisherPort).publishEvent(Mockito.any(Event.class));
		
		manageEventVisitor.visit(event);
		
		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(databaseSequenceAdapter, times(1)).generateSequence();
		Mockito.verify(publisherPort, times(1)).publishEvent(Mockito.any(Event.class));
	}

	@Test
	void ManageEventVisitor_visit_ReservationUpdatedEvent_ReturnVoid() {
		
		ReservationUpdatedEvent event = new ReservationUpdatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.when(databaseSequenceAdapter.generateSequence()).thenReturn(7L);
		Mockito.doNothing().when(publisherPort).publishEvent(Mockito.any(Event.class));
		
		manageEventVisitor.visit(event);
		
		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(databaseSequenceAdapter, times(1)).generateSequence();
		Mockito.verify(publisherPort, times(1)).publishEvent(Mockito.any(Event.class));
	}

	@Test
	void ManageEventVisitor_visit_ReservationDeletedEvent_ReturnVoid() {
		
		ReservationDeletedEvent event = new ReservationDeletedEvent(
				databaseSequence,
				reservationId
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.when(databaseSequenceAdapter.generateSequence()).thenReturn(7L);
		Mockito.doNothing().when(publisherPort).publishEvent(Mockito.any(Event.class));
		
		manageEventVisitor.visit(event);
		
		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(databaseSequenceAdapter, times(1)).generateSequence();
		Mockito.verify(publisherPort, times(1)).publishEvent(Mockito.any(Event.class));
	}

	@Test
	void ManageEventVisitor_visit_PaymentCreatedEvent_ReturnVoid() {
		
		PaymentCreatedEvent event = new PaymentCreatedEvent(
				databaseSequence,
				paymentId,
				reservationId,
				clientId,
				flightId
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		manageEventVisitor.visit(event);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void ManageEventVisitor_visit_PaymentUpdatedEvent_ReturnVoid() {
		
		PaymentUpdatedEvent event = new PaymentUpdatedEvent(
				databaseSequence,
				paymentId,
				reservationId,
				clientId,
				flightId
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		manageEventVisitor.visit(event);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

	@Test
	void ManageEventVisitor_visit_PaymentDeletedEvent_ReturnVoid() {
		
		PaymentDeletedEvent event = new PaymentDeletedEvent(
				databaseSequence,
				paymentId
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		
		manageEventVisitor.visit(event);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

}
