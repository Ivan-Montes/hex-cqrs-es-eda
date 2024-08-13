package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.config.ApplicationConstant;
import dev.ime.visitor.Visitor;

public final class ReservationDeletedEvent extends Event{

	private final UUID reservationId;

	@JsonCreator
	public ReservationDeletedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("reservationId")UUID reservationId) {
		super(ApplicationConstant.CAT_RESERV, ApplicationConstant.RESERVATION_DELETED, sequence);
		this.reservationId = reservationId;
	}

	public UUID getReservationId() {
		return reservationId;
	}

	@Override
	public String toString() {
		return "ReservationDeletedEvent [reservationId=" + reservationId + ", eventId=" + eventId + ", eventCategory="
				+ eventCategory + ", eventType=" + eventType + ", timeInstant=" + timeInstant + ", sequence=" + sequence
				+ "]";
	}	

	@Override
	public void accept(Visitor visitor) {
		
		visitor.visit(this);
		
	}
	
}
