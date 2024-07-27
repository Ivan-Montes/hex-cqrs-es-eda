package dev.ime.infrastructure.adapter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.config.LoggerUtil;

@ExtendWith(MockitoExtension.class)
class KafkaPublisherAdapterTest {

	@Mock
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Mock
	private LoggerUtil loggerUtil;	

	@InjectMocks
	private KafkaPublisherAdapter kafkaPublisherAdapter;
	
	private final Long databaseSequence = 11L;
	private final UUID planeId = UUID.randomUUID();
	
	@Mock
	private SendResult<String, Object> sendResult;

	@SuppressWarnings("unchecked")
	@Test
	void KafkaPublisherAdapter_publishEvent_ReturnVoid() {
		
		Mockito.doNothing().when(loggerUtil).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		PlaneDeletedEvent event = new PlaneDeletedEvent(
				databaseSequence,
				planeId
		);
		CompletableFuture<SendResult<String, Object>> completableFuture = new CompletableFuture<>();
		completableFuture.complete(sendResult);
        Mockito.when(kafkaTemplate.send(Mockito.any(ProducerRecord.class))).thenReturn(completableFuture);
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>("topic", "key", "value");
	    Mockito.when(sendResult.getProducerRecord()).thenReturn(producerRecord);
		
	    kafkaPublisherAdapter.publishEvent(event);
	    
		Mockito.verify(loggerUtil, times(2)).logInfoAction(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
		verify(kafkaTemplate, times(1)).send(Mockito.any(ProducerRecord.class));
		verify(sendResult, times(2)).getProducerRecord();

	}

}
