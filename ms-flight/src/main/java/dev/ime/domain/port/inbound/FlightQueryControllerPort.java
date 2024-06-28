package dev.ime.domain.port.inbound;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

public interface FlightQueryControllerPort<T> {

	ResponseEntity<List<T>> getAll(Integer page, Integer size);
	ResponseEntity<T>getById(UUID id);
	
}
