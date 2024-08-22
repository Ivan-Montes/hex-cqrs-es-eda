package dev.ime.kafka;

import static org.mockito.Mockito.times;

import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.event.Event;
import dev.ime.application.event.EventProcessor;
import dev.ime.application.event.PaymentCreatedEvent;
import dev.ime.config.ApplicationConstant;
import dev.ime.config.LoggerUtil;

@ExtendWith(MockitoExtension.class)
class KafkaPaymentSubscriberAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private EventProcessor eventProcessor;

	@InjectMocks
	private KafkaPaymentSubscriberAdapter kafkaReservationSubscriberAdapter;

	private final UUID paymentId = UUID.randomUUID();
	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private final Long databaseSequence = 37L;
	
	@Test
	void KafkaReservationSubscriberAdapter_onMessage_ReturnVoid() {
		
		PaymentCreatedEvent event = new PaymentCreatedEvent(
				databaseSequence,
				paymentId,
				reservationId,
				clientId,
				flightId				
				);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.PAYMENT_CREATED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(eventProcessor).processEvent(Mockito.any(Event.class));
		
		kafkaReservationSubscriberAdapter.onMessage(consumerRecord);

		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(eventProcessor, times(1)).processEvent(Mockito.any(Event.class));
	}

}
