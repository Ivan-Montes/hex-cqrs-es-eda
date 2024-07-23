package dev.ime.domain.port.outbound;

import dev.ime.domain.event.Event;

public interface GenericProjectorPort {
	
	void create(Event event);
	void update(Event event);
	void deleteById(Event event);
	
}
