package dev.ime.infrastructure.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.ime.domain.event.Event;
import dev.ime.domain.port.outbound.FlightNoSqlWriteRepositoryPort;
import dev.ime.infrastructure.repository.write.FlightNoSqlWriteRepository;

@Repository
public class FlightNoSqlWriteRepositoryAdapter implements FlightNoSqlWriteRepositoryPort {

	private final FlightNoSqlWriteRepository flightNoSqlWriteRepository;

	public FlightNoSqlWriteRepositoryAdapter(FlightNoSqlWriteRepository flightNoSqlWriteRepository) {
		super();
		this.flightNoSqlWriteRepository = flightNoSqlWriteRepository;
	}

	@Override
	public Optional<Event> save(Event event) {
		
		return Optional.ofNullable(flightNoSqlWriteRepository.save(event));
		
	}
	
}
