package dev.ime.domain.port.outbound;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.ime.domain.model.Flight;

public interface FlightNoSqlReadRepositoryPort {

	List<Flight> findAll(Integer page, Integer size);
	Optional<Flight> findById(UUID id);
	
}
