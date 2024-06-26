package dev.ime.application.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.GetByIdFlightQuery;
import dev.ime.domain.model.Flight;
import dev.ime.domain.port.outbound.FlightNoSqlReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class GetByIdFlightQueryHandler implements QueryHandler<Optional<Flight>>  {

	private final FlightNoSqlReadRepositoryPort flightNoSqlReadRepositoryPort;

	public GetByIdFlightQueryHandler(FlightNoSqlReadRepositoryPort flightNoSqlReadRepositoryPort) {
		super();
		this.flightNoSqlReadRepositoryPort = flightNoSqlReadRepositoryPort;
	}
	@Override
	public Optional<Flight> handle(Query query) {
		
		if ( query instanceof GetByIdFlightQuery getByIdFlightQuery) {
			
			UUID flightId = getByIdFlightQuery.flightId();
			Flight flight = validateFlightExists(flightId);
			
			return Optional.ofNullable(flight);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
			
		}
		
	}	

	private Flight validateFlightExists(UUID flightId) {

		Optional<Flight> optFlight = flightNoSqlReadRepositoryPort.findById(flightId);
		
		if ( optFlight.isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.FLIGHTID,String.valueOf(flightId)));
		}
		
		return optFlight.get();
	}
}
