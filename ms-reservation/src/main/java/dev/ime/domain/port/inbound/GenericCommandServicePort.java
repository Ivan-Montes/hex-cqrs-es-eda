package dev.ime.domain.port.inbound;

import java.util.Optional;
import java.util.UUID;

import dev.ime.domain.event.Event;

public interface GenericCommandServicePort<T> {

	Optional<Event> create(T dto);
	Optional<Event> update(UUID id, T dto);
	Optional<Event> deleteById(UUID id);
	
}
