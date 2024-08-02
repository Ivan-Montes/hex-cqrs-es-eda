package dev.ime.application.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.domain.event.Event;

public final class ClientCreatedEvent extends Event{

	private final UUID clientId;
    private final String name; 
    private final String lastname; 

    @JsonCreator
	public ClientCreatedEvent(@JsonProperty("sequence")Long sequence, @JsonProperty("clientId")UUID clientId, @JsonProperty("name")String name, @JsonProperty("lastname")String lastname) {
		super(ApplicationConstant.CAT_CLIENT, ApplicationConstant.CLIENT_CREATED, sequence);
		this.clientId = clientId;
		this.name = name;	
		this.lastname = lastname;	
	}

	public UUID getClientId() {
		return clientId;
	}

	public String getName() {
		return name;
	} 

	
	public String getLastname() {
		return lastname;
	}

	@Override
	public String toString() {
		return "ClientCreatedEvent [clientId=" + clientId + ", name=" + name + ", lastname=" + lastname + ", eventId="
				+ eventId + ", eventCategory=" + eventCategory + ", eventType=" + eventType + ", timeInstant="
				+ timeInstant + ", sequence=" + sequence + "]";
	}	
	
}
