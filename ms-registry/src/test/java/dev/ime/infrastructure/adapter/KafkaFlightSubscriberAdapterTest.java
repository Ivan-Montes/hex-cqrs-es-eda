package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.event.FlightCreatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.inbound.GenericCommandServicePort;

@ExtendWith(MockitoExtension.class)
class KafkaFlightSubscriberAdapterTest {

	@Mock
	private LoggerUtil loggerUtil;
	
	@Mock
	private GenericCommandServicePort<Event> genericCommandServicePort;
	
	@InjectMocks
	private KafkaFlightSubscriberAdapter kafkaFlightSubscriberAdapter;	

	private final Long databaseSequence = 11L;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();
	
	@Test
	void KafkaFlightSubscriberAdapter_onMessage_ReturnVoid_Branch() {
		
		FlightCreatedEvent event = new FlightCreatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId);
		ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>(
				ApplicationConstant.FLIGHT_CREATED,
				1,
				1L,
				"Ki",
				event
				);
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.doNothing().when(genericCommandServicePort).create(Mockito.any(Event.class));

		kafkaFlightSubscriberAdapter.onMessage(consumerRecord);
		
		Mockito.verify(loggerUtil, times(1)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		Mockito.verify(genericCommandServicePort, times(1)).create(Mockito.any(Event.class));
	}


}
