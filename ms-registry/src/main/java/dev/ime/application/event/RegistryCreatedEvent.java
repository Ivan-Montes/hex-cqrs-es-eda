package dev.ime.application.event;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public final class RegistryCreatedEvent extends Event{

    private final Map<String, Object> eventData;

    @JsonCreator
	public RegistryCreatedEvent(@JsonProperty("sequence")Long sequence,	@JsonProperty("eventData")Map<String, Object> eventData) {
		super(ApplicationConstant.CAT_REGISTRY, ApplicationConstant.REGISTRY_CREATED, sequence);
		this.eventData = eventData;
	}

	public Map<String, Object> getEventData() {
		return eventData;
	}

	@Override
	public String toString() {
		return "RegistryCreatedEvent [eventData=" + eventData + ", eventId=" + eventId + ", eventCategory="
				+ eventCategory + ", eventType=" + eventType + ", timeInstant=" + timeInstant + ", sequence=" + sequence
				+ "]";
	}
    
}
