package dev.ime.domain.port.outbound;

import java.util.Optional;

import dev.ime.domain.event.Event;

public interface EventNoSqlWriteRepositoryPort {

	Optional<Event> save(Event event);
	
}
