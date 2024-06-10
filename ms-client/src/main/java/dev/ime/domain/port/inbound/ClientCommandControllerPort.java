package dev.ime.domain.port.inbound;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import dev.ime.domain.event.Event;

public interface ClientCommandControllerPort<T> {

	ResponseEntity<Event>create(T dto);
	ResponseEntity<Event>update(UUID id, T dto);
	ResponseEntity<Event>deleteById(UUID id);
	
}
