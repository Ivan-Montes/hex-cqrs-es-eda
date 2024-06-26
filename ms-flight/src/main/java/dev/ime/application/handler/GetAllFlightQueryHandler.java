package dev.ime.application.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.usecase.GetAllFlightQuery;
import dev.ime.domain.model.Flight;
import dev.ime.domain.port.outbound.FlightNoSqlReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class GetAllFlightQueryHandler  implements QueryHandler<List<Flight>>{

	private final FlightNoSqlReadRepositoryPort flightNoSqlReadRepositoryPort;
	
	public GetAllFlightQueryHandler(FlightNoSqlReadRepositoryPort flightNoSqlReadRepositoryPort) {
		super();
		this.flightNoSqlReadRepositoryPort = flightNoSqlReadRepositoryPort;
	}

	@Override
	public List<Flight> handle(Query query) {
		
		if (query instanceof GetAllFlightQuery) {
			
			return flightNoSqlReadRepositoryPort.findAll();
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
		
		}
		
	}

}
