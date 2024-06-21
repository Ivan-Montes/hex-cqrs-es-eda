package dev.ime.domain.port.outbound;


import java.util.UUID;

import dev.ime.domain.event.Event;


public interface PlaneRedisProjectorPort {

	boolean existById(UUID id);
	void save(Event event);
	void deleteById(Event event);
	
}
