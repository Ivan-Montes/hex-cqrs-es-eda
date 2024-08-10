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
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.GenericCommandServicePort;

@ExtendWith(MockitoExtension.class)
class KafkaClientSubscriberAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private GenericCommandServicePort<Event> genericCommandServicePort;
	
	@InjectMocks
	private KafkaClientSubscriberAdapter kafkaClientSubscriberAdapter;

	private final UUID id = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private final Long databaseSequence = 37L;
	
	@Test
	void KafkaClientSubscriberAdapter_onMessage_ReturnVoid() {
		
		ClientCreatedEvent event = new ClientCreatedEvent(databaseSequence, id, name, lastname);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.CLIENT_CREATED,
                1,
                1L,
                "Key",
                event);		
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(genericCommandServicePort).create(Mockito.any(Event.class));
		
		kafkaClientSubscriberAdapter.onMessage(consumerRecord);

		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(genericCommandServicePort, times(1)).create(Mockito.any(Event.class));
	}

}
