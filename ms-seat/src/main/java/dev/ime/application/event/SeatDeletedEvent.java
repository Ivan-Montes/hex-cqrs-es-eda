package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public final class SeatDeletedEvent extends Event {

	private final UUID seatId;

    @JsonCreator
	public SeatDeletedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("seatId")UUID seatId) {
		super(ApplicationConstant.CAT_SEAT, ApplicationConstant.SEAT_DELETED, sequence);
		this.seatId = seatId;
	}

	public UUID getSeatId() {
		return seatId;
	}

	@Override
	public String toString() {
		return "SeatDeletedEvent [seatId=" + seatId + ", eventId=" + eventId + ", eventCategory=" + eventCategory
				+ ", eventType=" + eventType + ", timeInstant=" + timeInstant + ", sequence=" + sequence + "]";
	}

}
