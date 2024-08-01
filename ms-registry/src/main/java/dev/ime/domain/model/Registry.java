package dev.ime.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class Registry {
	
	private UUID eventId;	
	private String eventCategory;
	private String eventType;
	private Instant timeInstant;
	private Long sequence; 
    private Map<String, Object> eventData;
    
	public Registry() {
		super();
	}

	public Registry(UUID eventId, String eventCategory, String eventType, Instant timeInstant, Long sequence,
			Map<String, Object> eventData) {
		super();
		this.eventId = eventId;
		this.eventCategory = eventCategory;
		this.eventType = eventType;
		this.timeInstant = timeInstant;
		this.sequence = sequence;
		this.eventData = eventData;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
	}

	public String getEventCategory() {
		return eventCategory;
	}

	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Instant getTimeInstant() {
		return timeInstant;
	}

	public void setTimeInstant(Instant timeInstant) {
		this.timeInstant = timeInstant;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public Map<String, Object> getEventData() {
		return eventData;
	}

	public void setEventData(Map<String, Object> eventData) {
		this.eventData = eventData;
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventCategory, eventData, eventId, eventType, sequence, timeInstant);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Registry other = (Registry) obj;
		return Objects.equals(eventCategory, other.eventCategory) && Objects.equals(eventData, other.eventData)
				&& Objects.equals(eventId, other.eventId) && Objects.equals(eventType, other.eventType)
				&& Objects.equals(sequence, other.sequence)
				&& Objects.equals(timeInstant, other.timeInstant);
	}

	@Override
	public String toString() {
		return "Registry [eventId=" + eventId + ", eventCategory=" + eventCategory + ", eventType=" + eventType
				+ ", timeInstant=" + timeInstant + ", sequence=" + sequence + ", eventData=" + eventData + "]";
	}
    
}
