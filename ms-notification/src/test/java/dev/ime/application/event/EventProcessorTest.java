package dev.ime.application.event;

import static org.mockito.Mockito.times;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.config.LoggerUtil;
import dev.ime.db.DatabaseSequenceAdapter;
import dev.ime.kafka.PublisherPort;

@ExtendWith(MockitoExtension.class)
class EventProcessorTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private PublisherPort publisherPort;
	
	@Mock	
	private DatabaseSequenceAdapter databaseSequenceAdapter;
	
	@InjectMocks
	private EventProcessor eventProcessor;

	private final UUID paymentId = UUID.randomUUID();
	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private final Long databaseSequence = 52L;
	
	@Test
	void EventProcessor_processEvent_PaymentCreatedEvent_ReturnVoid() {
		
		PaymentCreatedEvent event = new PaymentCreatedEvent(
				databaseSequence,
				paymentId,
				reservationId,
				clientId,
				flightId				
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(publisherPort).publishEvent(Mockito.any(Event.class));

		eventProcessor.processEvent((Event)event);		

		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(publisherPort, times(1)).publishEvent(Mockito.any(Event.class));
	}

	@Test
	void EventProcessor_processEvent_PaymentUpdatedEvent_ReturnVoid() {
		
		PaymentUpdatedEvent event = new PaymentUpdatedEvent(
				databaseSequence,
				paymentId,
				reservationId,
				clientId,
				flightId				
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(publisherPort).publishEvent(Mockito.any(Event.class));

		eventProcessor.processEvent((Event)event);		

		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(publisherPort, times(1)).publishEvent(Mockito.any(Event.class));
	}

	@Test
	void EventProcessor_processEvent_PaymentDeletedEvent_ReturnVoid() {
		
		PaymentDeletedEvent event = new PaymentDeletedEvent(
				databaseSequence,
				paymentId		
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(publisherPort).publishEvent(Mockito.any(Event.class));

		eventProcessor.processEvent((Event)event);		

		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(publisherPort, times(1)).publishEvent(Mockito.any(Event.class));
	}
	

}
