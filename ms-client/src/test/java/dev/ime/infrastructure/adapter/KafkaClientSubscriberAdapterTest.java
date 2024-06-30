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
import dev.ime.application.event.ClientCreatedEvent;
import dev.ime.application.event.ClientDeletedEvent;
import dev.ime.application.event.ClientUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.ClientProjectorPort;

@ExtendWith(MockitoExtension.class)
class KafkaClientSubscriberAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;

	@Mock
	private ClientProjectorPort clientProjectorPort;
	
	@InjectMocks
	private KafkaClientSubscriberAdapter kafkaClientSubscriberAdapter;

	private final UUID id = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private final Long databaseSequence = 9L;

	@Test
	void KafkaClientSubscriberAdapter_onMessage_ReturnVoid_BranchCLIENT_CREATED() {
		
		ClientCreatedEvent event = new ClientCreatedEvent(databaseSequence, id, name, lastname);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.CLIENT_CREATED,
                1,
                1L,
                "Key",
                event);		
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaClientSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(clientProjectorPort, times(1)).create(Mockito.any(Event.class));
	}

	@Test
	void KafkaClientSubscriberAdapter_onMessage_ReturnVoid_BranchCLIENT_UPDATED() {
		
		ClientUpdatedEvent event = new ClientUpdatedEvent(databaseSequence, id, name, lastname);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.CLIENT_UPDATED,
                1,
                1L,
                "Key",
                event);		
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaClientSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(clientProjectorPort, times(1)).update(Mockito.any(Event.class));
	}

	@Test
	void KafkaClientSubscriberAdapter_onMessage_ReturnVoid_BranchCLIENT_DELETED() {
		
		ClientDeletedEvent event = new ClientDeletedEvent(databaseSequence, id);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.CLIENT_DELETED,
                1,
                1L,
                "Key",
                event);		
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaClientSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(clientProjectorPort, times(1)).deleteById(Mockito.any(Event.class));
	}

	@Test
	void KafkaClientSubscriberAdapter_onMessage_ReturnVoid_BranchDefault() {
		
		ClientDeletedEvent event = new ClientDeletedEvent(databaseSequence, id);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.UNKNOWDATA,
                1,
                1L,
                "Key",
                event);		
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

		kafkaClientSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
	}
	

}
