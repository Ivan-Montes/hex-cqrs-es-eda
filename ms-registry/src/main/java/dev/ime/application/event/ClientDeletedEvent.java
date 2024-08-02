package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public final class ClientDeletedEvent extends Event{

	private final UUID clientId;
	
	@JsonCreator
	public ClientDeletedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("clientId")UUID clientId) {
		super(ApplicationConstant.CAT_CLIENT, ApplicationConstant.CLIENT_DELETED, sequence);
		this.clientId = clientId;
	}

	public UUID getClientId() {
		return clientId;
	}

	@Override
	public String toString() {
		return "ClientDeletedEvent [clientId=" + clientId + ", eventId=" + eventId + ", eventCategory=" + eventCategory
				+ ", eventType=" + eventType + ", timeInstant=" + timeInstant + ", sequence=" + sequence + "]";
	}	
	
}
