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
import dev.ime.application.event.PlaneCreatedEvent;
import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.application.event.PlaneUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.GenericProjectorPort;

@ExtendWith(MockitoExtension.class)
class KafkaPlaneSubscriberAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;

	@Mock
	private GenericProjectorPort genericProjectorPort;
	
	@InjectMocks
	private KafkaPlaneSubscriberAdapter kafkaSubscriberAdapter;	

	private final Long databaseSequence = 27L;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;	

	@Test
	void KafkaSubscriberAdapter_onMessagePlaneCreated_ReturnVoid() {
		
		PlaneCreatedEvent event = new PlaneCreatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.PLANE_CREATED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(genericProjectorPort, times(1)).create(Mockito.any(Event.class));
	
	}

	@Test
	void KafkaSubscriberAdapter_onMessagePlaneUpdated_ReturnVoid() {
		
		PlaneUpdatedEvent event = new PlaneUpdatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.PLANE_UPDATED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(genericProjectorPort, times(1)).update(Mockito.any(Event.class));
		
	}

	@Test
	void KafkaSubscriberAdapter_onMessagePlaneDeleted_ReturnVoid() {
		
		PlaneDeletedEvent event = new PlaneDeletedEvent(
				databaseSequence,
				planeId
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.PLANE_DELETED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(genericProjectorPort, times(1)).deleteById(Mockito.any(Event.class));
		
	}

	@Test
	void KafkaSubscriberAdapter_onMessageDefault_ReturnVoid() {
		
		PlaneDeletedEvent event = new PlaneDeletedEvent(
				databaseSequence,
				planeId
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.UNKNOWDATA,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	
}
