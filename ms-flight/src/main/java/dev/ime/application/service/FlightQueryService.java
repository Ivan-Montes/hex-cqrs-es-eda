package dev.ime.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.dispatch.FlightQueryDispatcher;
import dev.ime.application.usecase.GetAllFlightQuery;
import dev.ime.application.usecase.GetByIdFlightQuery;
import dev.ime.domain.model.Flight;
import dev.ime.domain.port.inbound.FlightQueryServicePort;
import dev.ime.domain.query.QueryHandler;

@Service
public class FlightQueryService implements FlightQueryServicePort{

	private final FlightQueryDispatcher flightQueryDispatcher;
	
	public FlightQueryService(FlightQueryDispatcher flightQueryDispatcher) {
		super();
		this.flightQueryDispatcher = flightQueryDispatcher;
	}

	@Override
	public List<Flight> getAll(Integer page, Integer size) {
		
		GetAllFlightQuery getAllQuery = new GetAllFlightQuery(page, size);
		QueryHandler<List<Flight>> queryHandler = flightQueryDispatcher.getQueryHandler(getAllQuery);
		
		return queryHandler.handle(getAllQuery);
	}	

	@Override
	public Optional<Flight> getById(UUID id) {
		
		GetByIdFlightQuery getByIdQuery = new GetByIdFlightQuery(id);
		QueryHandler<Optional<Flight>> queryHandler = flightQueryDispatcher.getQueryHandler(getByIdQuery);
		
		return queryHandler.handle(getByIdQuery);
	}

}
