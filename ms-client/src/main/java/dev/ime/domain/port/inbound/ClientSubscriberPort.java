package dev.ime.domain.port.inbound;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ClientSubscriberPort {

	void onMessage(ConsumerRecord<String, Object> consumerRecord);
	
}
