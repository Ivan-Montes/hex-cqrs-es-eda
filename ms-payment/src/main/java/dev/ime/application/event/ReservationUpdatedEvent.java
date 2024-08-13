package dev.ime.application.event;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.config.ApplicationConstant;
import dev.ime.visitor.Visitor;

public final class ReservationUpdatedEvent extends Event {
	
	private final UUID reservationId;
	private final UUID clientId;
	private final UUID flightId;
	private final Set<UUID> seatIdsSet;

	@JsonCreator
	public ReservationUpdatedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("reservationId")UUID reservationId,
			@JsonProperty("clientId")UUID clientId, @JsonProperty("flightId")UUID flightId, @JsonProperty("seatIdsSet")Set<UUID> seatIdsSet) {
		super(ApplicationConstant.CAT_RESERV, ApplicationConstant.RESERVATION_UPDATED, sequence);
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
		return "ReservationUpdatedEvent [reservationId=" + reservationId + ", clientId=" + clientId + ", flightId="
				+ flightId + ", seatIdsSet=" + seatIdsSet + ", eventId=" + eventId + ", eventCategory=" + eventCategory
				+ ", eventType=" + eventType + ", timeInstant=" + timeInstant + ", sequence=" + sequence + "]";
	}

	@Override
	public void accept(Visitor visitor) {
		
		visitor.visit(this);
		
	}

}
