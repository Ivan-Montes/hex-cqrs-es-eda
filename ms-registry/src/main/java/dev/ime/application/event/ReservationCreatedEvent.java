package dev.ime.application.event;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public final class ReservationCreatedEvent extends Event {
	
	private final UUID reservationId;
	private final UUID clientId;
	private final UUID flightId;
	private final Set<UUID> seatIdsSet;

	@JsonCreator
	public ReservationCreatedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("reservationId")UUID reservationId,
			@JsonProperty("clientId")UUID clientId, @JsonProperty("flightId")UUID flightId, @JsonProperty("seatIdsSet")Set<UUID> seatIdsSet) {
		super(ApplicationConstant.CAT_RESERV, ApplicationConstant.RESERVATION_CREATED, sequence);
		this.reservationId = reservationId;
		this.clientId = clientId;
		this.flightId = flightId;
		this.seatIdsSet = seatIdsSet;
	}

	public UUID getReservationId() {
		return reservationId;
	}

	public UUID getClientId() {
		return clientId;
	}

	public UUID getFlightId() {
		return flightId;
	}

	public Set<UUID> getSeatIdsSet() {
		return seatIdsSet;
	}

	@Override
	public String toString() {
		return "ReservationCreatedEvent [reservationId=" + reservationId + ", clientId=" + clientId + ", flightId="
				+ flightId + ", seatIdsSet=" + seatIdsSet + ", eventId=" + eventId + ", eventCategory=" + eventCategory
				+ ", eventType=" + eventType + ", timeInstant=" + timeInstant + ", sequence=" + sequence + "]";
	}

}
