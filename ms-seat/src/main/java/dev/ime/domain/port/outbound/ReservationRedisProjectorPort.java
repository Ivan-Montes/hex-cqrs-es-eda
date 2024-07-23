package dev.ime.domain.port.outbound;

import java.util.UUID;

import dev.ime.domain.event.Event;

public interface ReservationRedisProjectorPort {
	
	void create(Event event);
	void update(Event event);
	void deleteById(Event event);
	boolean existsReservationRedisEntityBySeatId(UUID seatId);
	
}
