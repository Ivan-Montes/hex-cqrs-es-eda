package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.util.HashMap;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.RegistryCreatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.BaseProjectorPort;

@ExtendWith(MockitoExtension.class)
class KafkaRegistrySubscriberAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private BaseProjectorPort registryBaseProjectorPort;
	
	@InjectMocks
	private KafkaRegistrySubscriberAdapter kafkaRegistrySubscriberAdapter;

	@Test
	void KafkaRegistrySubscriberAdapter_onMessage_ReturnVoid() {
		
		RegistryCreatedEvent event = new RegistryCreatedEvent(
				37L,
				new HashMap<>()
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.CLIENT_CREATED,
                1,
                1L,
                "Key",
                event);		
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(registryBaseProjectorPort).create(Mockito.any(Event.class));
		
		kafkaRegistrySubscriberAdapter.onMessage(consumerRecord);

		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(registryBaseProjectorPort, times(1)).create(Mockito.any(Event.class));
	}

}
