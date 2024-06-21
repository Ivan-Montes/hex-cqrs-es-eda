package dev.ime.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import dev.ime.config.FlightMapper;
import dev.ime.domain.model.Flight;
import dev.ime.domain.port.outbound.FlightNoSqlReadRepositoryPort;
import dev.ime.infrastructure.repository.read.FlightNoSqlReadRepository;

@Repository
public class FlightNoSqlReadRepositoryAdapter implements FlightNoSqlReadRepositoryPort{

	private final FlightNoSqlReadRepository flightNoSqlReadRepository;
	private final FlightMapper flightMapper;
	
	public FlightNoSqlReadRepositoryAdapter(FlightNoSqlReadRepository flightNoSqlReadRepository,
			FlightMapper flightMapper) {
		super();
		this.flightNoSqlReadRepository = flightNoSqlReadRepository;
		this.flightMapper = flightMapper;
	}

	@Override
	public List<Flight> findAll() {
		
		return flightMapper.fromListMongoToListDomain( flightNoSqlReadRepository.findAll() );

	}

	@Override
	public Optional<Flight> findById(UUID id) {
		
		return flightNoSqlReadRepository.findFirstByFlightId(id).map(flightMapper::fromMongoToDomain);

	}

}
