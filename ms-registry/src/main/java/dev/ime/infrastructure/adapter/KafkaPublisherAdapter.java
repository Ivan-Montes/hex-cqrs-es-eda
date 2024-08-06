package dev.ime.infrastructure.adapter;

import java.util.concurrent.CompletableFuture;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import dev.ime.config.LoggerUtil;
import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.PublisherPort;

@Service
public class KafkaPublisherAdapter implements PublisherPort{

	private final KafkaTemplate<String, Object> kafkaTemplate;	
	private final LoggerUtil loggerUtil;	
	
	public KafkaPublisherAdapter(KafkaTemplate<String, Object> kafkaTemplate, LoggerUtil loggerUtil) {
		super();
		this.kafkaTemplate = kafkaTemplate;
		this.loggerUtil = loggerUtil;
	}

	@Override
	public void publishEvent(Event event) {
		
		logInfo("publishEvent", event.toString());
		CompletableFuture<SendResult<String, Object>> completableFuture = kafkaTemplate.send(new ProducerRecord<>(event.getEventType(), event));
		completableFuture.whenComplete( (result, ex) -> {
			if (ex == null) {
	            handleSuccess(result);
	        }
	        else {
	            handleFailure(result, ex);
	        }
		});	
		
	}

	private void handleSuccess(SendResult<String, Object> result) {
		logInfo("handleSuccess", result.getProducerRecord().topic() + "]:[" + result.getProducerRecord().value() );
	}

    private void handleFailure(SendResult<String, Object> result, Throwable ex) {
    	logInfo("handleFailure", result.getProducerRecord().topic() + "]:[" + result.getProducerRecord().value() + "]:[" + ex.getMessage() );
    }

	private void logInfo(String action, String clientInfo) {
		
	    loggerUtil.logInfoAction(this.getClass().getSimpleName(), action, clientInfo);
	    
	}

}
