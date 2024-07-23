package dev.ime.domain.port.inbound;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface SubscriberPort {
	
	void onMessage(ConsumerRecord<String, Object> consumerRecord);

}
