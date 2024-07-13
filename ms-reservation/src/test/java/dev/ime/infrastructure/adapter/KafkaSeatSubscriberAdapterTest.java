package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

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
import dev.ime.application.event.SeatCreatedEvent;
import dev.ime.application.event.SeatDeletedEvent;
import dev.ime.application.event.SeatUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.BaseProjectorPort;
import dev.ime.domain.port.outbound.ExtendedProjectorPort;

@ExtendWith(MockitoExtension.class)
class KafkaSeatSubscriberAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private BaseProjectorPort seatBaseProjectorPort;
	
	@Mock
	private ExtendedProjectorPort seatExtendedProjectorPort;

	@InjectMocks
	private KafkaSeatSubscriberAdapter kafkaSeatSubscriberAdapter;

	private final Long databaseSequence = 27L;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();
	
	@Test
	void KafkaSeatSubscriberAdapter_onMessageSeatCreated_ReturnVoid() {
		
		SeatCreatedEvent event = new SeatCreatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.SEAT_CREATED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaSeatSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(seatBaseProjectorPort, times(1)).create(Mockito.any(Event.class));
	}

	@Test
	void KafkaSeatSubscriberAdapter_onMessageSeatUpdated_ReturnVoid() {

		SeatUpdatedEvent event = new SeatUpdatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.SEAT_UPDATED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaSeatSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(seatExtendedProjectorPort, times(1)).update(Mockito.any(Event.class));
	}

	@Test
	void KafkaSeatSubscriberAdapter_onMessageSeatDeleted_ReturnVoid() {

		SeatDeletedEvent event = new SeatDeletedEvent(
				databaseSequence,
				seatId
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.SEAT_DELETED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaSeatSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(seatBaseProjectorPort, times(1)).deleteById(event);
	}

	@Test
	void KafkaSeatSubscriberAdapter_onMessageDefault_ReturnVoid() {
		
		FlightDeletedEvent event = new FlightDeletedEvent(
				databaseSequence,
				UUID.randomUUID()
		);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.UNKNOWDATA,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaSeatSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}

}
