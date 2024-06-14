package dev.ime.infrastructure.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.ClientNoSqlWriteRepositoryPort;
import dev.ime.infrastructure.repository.write.ClientNoSqlWriteRepository;

@Repository
public class ClientNoSqlWriteRepositoryAdapter implements ClientNoSqlWriteRepositoryPort{
	
	private final ClientNoSqlWriteRepository clientNoSqlWriteRepository;

	public ClientNoSqlWriteRepositoryAdapter(ClientNoSqlWriteRepository clientNoSqlWriteRepository) {
		super();
		this.clientNoSqlWriteRepository = clientNoSqlWriteRepository;
	}

	@Override
	public Optional<Event> save(Event event) {
		
		return Optional.ofNullable(clientNoSqlWriteRepository.save(event));
		
	}
	
}
