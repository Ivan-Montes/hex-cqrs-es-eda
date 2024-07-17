package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public final class SeatUpdatedEvent extends Event {

	private final UUID seatId;
	private final String seatNumber;
	private final UUID planeId;

    @JsonCreator
	public SeatUpdatedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("seatId")UUID seatId, @JsonProperty("seatNumber")String seatNumber,
			@JsonProperty("planeId")UUID planeId) {
		super(ApplicationConstant.CAT_SEAT, ApplicationConstant.SEAT_UPDATED, sequence);
		this.seatId = seatId;
		this.seatNumber = seatNumber;
		this.planeId = planeId;
	}

	public UUID getSeatId() {
		return seatId;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public UUID getPlaneId() {
		return planeId;
	}

	@Override
	public String toString() {
		return "SeatUpdatedEvent [seatId=" + seatId + ", seatNumber=" + seatNumber + ", planeId=" + planeId
				+ ", eventId=" + eventId + ", eventCategory=" + eventCategory + ", eventType=" + eventType
				+ ", timeInstant=" + timeInstant + ", sequence=" + sequence + "]";
	}

}
