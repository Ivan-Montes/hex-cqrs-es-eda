package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.ReservationCreatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.GenericCommandServicePort;

@ExtendWith(MockitoExtension.class)
class KafkaReservationSubscriberAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private GenericCommandServicePort<Event> genericCommandServicePort;
	
	@InjectMocks
	private KafkaReservationSubscriberAdapter kafkaReservationSubscriberAdapter;	

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet = new HashSet<>();
	private final Long databaseSequence = 37L;
	
	@Test
	void KafkaReservationSubscriberAdapter_onMessage_ReturnVoid_BranchRESERVATION_CREATED() {
		
		ReservationCreatedEvent event = new ReservationCreatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.RESERVATION_CREATED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(genericCommandServicePort).create(Mockito.any(Event.class));

		kafkaReservationSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(genericCommandServicePort, times(1)).create(Mockito.any(Event.class));
		
	}


}
