package dev.ime.kafka;

import dev.ime.application.event.Event;

public interface PublisherPort {

	void publishEvent(Event event);
	
}
