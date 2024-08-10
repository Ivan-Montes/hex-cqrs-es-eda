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
import dev.ime.application.event.NotificationCreatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.GenericCommandServicePort;

@ExtendWith(MockitoExtension.class)
class KafkaNotificationSubscriberAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private GenericCommandServicePort<Event> genericCommandServicePort;
	
	@InjectMocks
	private KafkaNotificationSubscriberAdapter kafkaNotificationSubscriberAdapter;

	private final Long databaseSequence = 11L;
	private final UUID notificationId = UUID.randomUUID();
	private final UUID paymentId = UUID.randomUUID();
	
	@Test
	void KafkaNotificationSubscriberAdapter_onMessage_ReturnVoid() {
		
		NotificationCreatedEvent event = new NotificationCreatedEvent(
				databaseSequence,
				notificationId,
				paymentId
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.NOTIFICATION_CREATED,
                1,
                1L,
                "Key",
                event);	
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(genericCommandServicePort).create(Mockito.any(Event.class));
		
		kafkaNotificationSubscriberAdapter.onMessage(consumerRecord);

		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(genericCommandServicePort, times(1)).create(Mockito.any(Event.class));
	}

}
