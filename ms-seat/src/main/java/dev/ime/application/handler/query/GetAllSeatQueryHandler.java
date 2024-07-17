package dev.ime.application.handler.query;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.usecase.query.GetAllSeatQuery;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class GetAllSeatQueryHandler implements QueryHandler<List<Seat>>{

	private final GenericNoSqlReadRepositoryPort<Seat> genericNoSqlReadRepositoryPort;

	public GetAllSeatQueryHandler(GenericNoSqlReadRepositoryPort<Seat> genericNoSqlReadRepositoryPort) {
		super();
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
	}

	@Override
	public List<Seat> handle(Query query) {
		
		if (query instanceof GetAllSeatQuery getAllSeatQuery) {
			
			return genericNoSqlReadRepositoryPort.findAll(				
					getAllSeatQuery.page(),
					getAllSeatQuery.size()
					);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
		
		}

	}	
	
}
