package dev.ime.domain.port.outbound;

import java.util.List;
import java.util.UUID;


public interface SeatSpecificNoSqlReadRepositoryPort<T> {

	List<T> findByPlaneId(UUID planeId);
	
}
