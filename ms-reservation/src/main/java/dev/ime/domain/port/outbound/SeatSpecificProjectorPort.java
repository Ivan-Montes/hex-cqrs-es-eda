package dev.ime.domain.port.outbound;

import java.util.List;
import java.util.UUID;

public interface SeatSpecificProjectorPort {

	List<UUID>findSeatByPlaneId(UUID id);
	
}
