package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public final class FlightDeletedEvent extends Event{

	private final UUID flightId;

	@JsonCreator
	public FlightDeletedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("flightId")UUID flightId) {
		super(ApplicationConstant.CAT_FLIGHT, ApplicationConstant.FLIGHT_DELETED, sequence);
		this.flightId = flightId;
	}

	public UUID getFlightId() {
		return flightId;
	}

	@Override
	public String toString() {
		return "FlightDeletedEvent [flightId=" + flightId + ", eventId=" + eventId + ", eventCategory=" + eventCategory
				+ ", eventType=" + eventType + ", timeInstant=" + timeInstant + ", sequence=" + sequence + "]";
	}	
	
}
