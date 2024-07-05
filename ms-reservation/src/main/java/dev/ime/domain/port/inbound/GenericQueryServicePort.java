package dev.ime.domain.port.inbound;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface GenericQueryServicePort<T> {

	List<T> getAll(Integer page, Integer size);
	Optional<T> getById(UUID id);
	
}
