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
import dev.ime.application.event.FlightDeletedEvent;
import dev.ime.application.event.ReservationCreatedEvent;
import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.application.event.ReservationUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.BaseProjectorPort;
import dev.ime.domain.port.outbound.ExtendedProjectorPort;

@ExtendWith(MockitoExtension.class)
class KafkaReservationSubscriberAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private BaseProjectorPort reservationBaseProjectorPort;	
	
	@Mock
	private ExtendedProjectorPort reservationExtendedProjectorPort;	

	@InjectMocks
	private KafkaReservationSubscriberAdapter kafkaReservationSubscriberAdapter;

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet = new HashSet<>();
	private final Long databaseSequence = 37L;
	
	
	@Test
	void KafkaReservationSubscriberAdapter_onMessageReservationCreated_ReturnVoid() {
		
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

		kafkaReservationSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(reservationBaseProjectorPort, times(1)).create(Mockito.any(Event.class));
		
	}

	@Test
	void KafkaReservationSubscriberAdapter_onMessageReservationUpdated_ReturnVoid() {
		
		ReservationUpdatedEvent event = new ReservationUpdatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.RESERVATION_UPDATED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaReservationSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(reservationExtendedProjectorPort, times(1)).update(Mockito.any(Event.class));
	}

	@Test
	void KafkaReservationSubscriberAdapter_onMessageReservationDeleted_ReturnVoid() {
		
		ReservationDeletedEvent event = new ReservationDeletedEvent(databaseSequence, reservationId);

		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.RESERVATION_DELETED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaReservationSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(reservationBaseProjectorPort, times(1)).deleteById(event);
	}

	@Test
	void KafkaSubscriberAdapter_onMessageDefault_ReturnVoid() {

		FlightDeletedEvent event = new FlightDeletedEvent(
				databaseSequence,
				flightId
		);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.UNKNOWDATA,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaReservationSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
}
