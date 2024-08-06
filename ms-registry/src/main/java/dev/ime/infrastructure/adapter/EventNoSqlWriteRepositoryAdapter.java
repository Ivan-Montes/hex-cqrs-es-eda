package dev.ime.infrastructure.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.EventNoSqlWriteRepositoryPort;
import dev.ime.infrastructure.repository.EventNoSqlWriteRepository;

@Repository
public class EventNoSqlWriteRepositoryAdapter implements EventNoSqlWriteRepositoryPort{

	private final EventNoSqlWriteRepository eventNoSqlWriteRepository;
	
	public EventNoSqlWriteRepositoryAdapter(EventNoSqlWriteRepository eventNoSqlWriteRepository) {
		super();
		this.eventNoSqlWriteRepository = eventNoSqlWriteRepository;
	}

	@Override
	public Optional<Event> save(Event event) {
		
		return Optional.ofNullable(eventNoSqlWriteRepository.save(event));
		
	}

}
