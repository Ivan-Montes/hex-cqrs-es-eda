package dev.ime.application.event;


import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EventTest {
	
	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private final UUID paymentId = UUID.randomUUID();
	private final Long databaseSequence = 37L;
	
	@Test
	void Event_Constructor_ReturnEvent() {
		
		Event event = new PaymentCreatedEvent(
				databaseSequence,
				paymentId,
				reservationId,
				clientId,
				flightId
				);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(event).isNotNull(),
				()-> Assertions.assertThat(event.getClass()).isEqualTo(PaymentCreatedEvent.class),
				()-> Assertions.assertThat(event.getEventId()).isNotNull(),
				()-> Assertions.assertThat(event.getEventCategory()).isNotEmpty(),
				()-> Assertions.assertThat(event.getEventType()).isNotEmpty(),
				()-> Assertions.assertThat(event.getTimeInstant()).isNotNull(),
				()-> Assertions.assertThat(event.getSequence()).isEqualByComparingTo(databaseSequence)				
				);	
	}

}
