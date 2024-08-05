package dev.ime.domain.port.outbound;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface GenericReadRepositoryPort<T> {

	List<T> findAll(Integer page, Integer size);
	Optional<T> findById(UUID id);
	
}
