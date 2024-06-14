package dev.ime.domain.port.inbound;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.ime.domain.model.Client;

public interface ClientQueryServicePort {
	
	List<Client>getAll();
	Optional<Client>getById(UUID id);
	
}
