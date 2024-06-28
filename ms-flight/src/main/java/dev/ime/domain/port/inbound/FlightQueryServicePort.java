package dev.ime.domain.port.inbound;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.ime.domain.model.Flight;

public interface FlightQueryServicePort {

	List<Flight> getAll(Integer page, Integer size);
	Optional<Flight> getById(UUID id);
	
}
