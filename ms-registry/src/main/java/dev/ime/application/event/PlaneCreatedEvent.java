package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public final class PlaneCreatedEvent extends Event {
	
	private final UUID planeId;
	private final String name;
	private final Integer capacity;

    @JsonCreator
	public PlaneCreatedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("planeId")UUID planeId, @JsonProperty("name")String name,
			@JsonProperty("capacity")Integer capacity) {
		super(ApplicationConstant.CAT_PLANE, ApplicationConstant.PLANE_CREATED, sequence);
		this.planeId = planeId;
		this.name = name;
		this.capacity = capacity;
	}

	public UUID getPlaneId() {
		return planeId;
	}

	public String getName() {
		return name;
	}

	public Integer getCapacity() {
		return capacity;
	}

	@Override
	public String toString() {
		return "PlaneCreatedEvent [planeId=" + planeId + ", name=" + name + ", capacity=" + capacity + ", eventId="
				+ eventId + ", eventCategory=" + eventCategory + ", eventType=" + eventType + ", timeInstant="
				+ timeInstant + ", sequence=" + sequence + "]";
	}
	
}
