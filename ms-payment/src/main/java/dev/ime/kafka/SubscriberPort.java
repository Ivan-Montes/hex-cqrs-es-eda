package dev.ime.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface SubscriberPort {

	void onMessage(ConsumerRecord<String, Object> consumerRecord);

}
