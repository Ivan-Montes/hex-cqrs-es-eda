package dev.ime.application.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.usecase.GetAllReservationQuery;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class GetAllReservationQueryHandler implements QueryHandler<List<Reservation>>{

	private final GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort;
	
	public GetAllReservationQueryHandler(GenericReadRepositoryPort<Reservation> genericNoSqlReadRepositoryPort) {
		super();
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
	}

	@Override
	public List<Reservation> handle(Query query) {
		
		if (query instanceof GetAllReservationQuery getAllReservationQuery) {
			
			return genericNoSqlReadRepositoryPort.findAll(				
					getAllReservationQuery.page(),
					getAllReservationQuery.size()
			);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
		
		}
	}

}
