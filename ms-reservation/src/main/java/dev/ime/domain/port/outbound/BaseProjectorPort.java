package dev.ime.domain.port.outbound;

import java.util.UUID;

import dev.ime.domain.event.Event;

public interface BaseProjectorPort {

	void create(Event event);
	void deleteById(Event event);
	boolean existsById(UUID id);
	
}
