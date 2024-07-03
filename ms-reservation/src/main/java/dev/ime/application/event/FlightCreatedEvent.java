package dev.ime.application.event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public final class FlightCreatedEvent extends Event {

	private final UUID flightId;
	private final String origin;
	private final String destiny;	
	private final LocalDate departureDate;
	private final LocalTime departureTime;
	private final UUID planeId;

    @JsonCreator
	public FlightCreatedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("flightId")UUID flightId, @JsonProperty("origin")String origin,
			@JsonProperty("destiny")String destiny, @JsonProperty("departureDate")LocalDate departureDate, @JsonProperty("departureTime")LocalTime departureTime, @JsonProperty("planeId")UUID planeId) {
		
		super(ApplicationConstant.CAT_FLIGHT, ApplicationConstant.FLIGHT_CREATED, sequence);
		this.flightId = flightId;
		this.origin = origin;
		this.destiny = destiny;
		this.departureDate = departureDate;
		this.departureTime = departureTime;
		this.planeId = planeId;
	}

	public UUID getFlightId() {
		return flightId;
	}

	public String getOrigin() {
		return origin;
	}

	public String getDestiny() {
		return destiny;
	}

	public LocalDate getDepartureDate() {
		return departureDate;
	}

	public LocalTime getDepartureTime() {
		return departureTime;
	}

	public UUID getPlaneId() {
		return planeId;
	}

	@Override
	public String toString() {
		return "FlightCreatedEvent [flightId=" + flightId + ", origin=" + origin + ", destiny=" + destiny
				+ ", departureDate=" + departureDate + ", departureTime=" + departureTime + ", planeId=" + planeId
				+ ", eventId=" + eventId + ", eventCategory=" + eventCategory + ", eventType=" + eventType
				+ ", timeInstant=" + timeInstant + ", sequence=" + sequence + "]";
	}	
	
}
