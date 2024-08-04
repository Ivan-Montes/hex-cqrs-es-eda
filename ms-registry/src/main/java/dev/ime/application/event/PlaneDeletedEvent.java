package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public final class PlaneDeletedEvent extends Event {

	private final UUID planeId;

    @JsonCreator
	public PlaneDeletedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("planeId")UUID planeId) {
		super(ApplicationConstant.CAT_PLANE, ApplicationConstant.PLANE_DELETED, sequence);
		this.planeId = planeId;
	}

	public UUID getPlaneId() {
		return planeId;
	}

	@Override
	public String toString() {
		return "PlaneDeletedEvent [planeId=" + planeId + ", eventId=" + eventId + ", eventCategory=" + eventCategory
				+ ", eventType=" + eventType + ", timeInstant=" + timeInstant + ", sequence=" + sequence + "]";
	}

}
