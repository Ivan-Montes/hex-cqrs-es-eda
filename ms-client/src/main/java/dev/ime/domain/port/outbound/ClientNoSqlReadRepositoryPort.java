package dev.ime.domain.port.outbound;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import dev.ime.domain.model.Client;

public interface ClientNoSqlReadRepositoryPort {
	
	List<Client>findAll(Integer page, Integer size);
	Optional<Client>findById(UUID id);
	
}
