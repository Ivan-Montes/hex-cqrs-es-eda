package dev.ime.domain.port.outbound;

import java.util.UUID;

import dev.ime.domain.event.Event;

public interface FlightRedisProjectorPort {
	
	boolean existFlightRedisEntityByPlaneId(UUID planeId);
	void save(Event event);
	void update(Event event);
	void deleteById(Event event);
	
}
