package dev.ime.application.event;

import java.time.Instant;
import java.util.UUID;


public abstract class Event {

	protected final UUID eventId;
	protected final String eventCategory;
	protected final String eventType;
	protected final Instant timeInstant;
	protected final Long sequence;    
    
	protected Event(String eventCategory, String eventType, Long sequence) {
		super();
        this.eventId = UUID.randomUUID();
        this.eventCategory = eventCategory;
		this.eventType = eventType;
        this.timeInstant = Instant.now();
        this.sequence = sequence;
    }

	public UUID getEventId() {
		return eventId;
	}

	public String getEventCategory() {
		return eventCategory;
	}

	public String getEventType() {
		return eventType;
	}

	public Instant getTimeInstant() {
		return timeInstant;
	}

	public Long getSequence() {
		return sequence;
	}

	@Override
	public String toString() {
		return "Event [eventId=" + eventId + ", eventCategory=" + eventCategory + ", eventType=" + eventType
				+ ", timeInstant=" + timeInstant + ", sequence=" + sequence + "]";
	}	
    
}
