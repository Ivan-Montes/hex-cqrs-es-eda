package dev.ime.application.handler.query;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.usecase.query.GetAllPlaneQuery;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class GetAllPlaneQueryHandler implements QueryHandler<List<Plane>>{
	
	private final GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort;

	public GetAllPlaneQueryHandler(GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort) {
		super();
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
	}

	@Override
	public List<Plane> handle(Query query) {
		
		if (query instanceof GetAllPlaneQuery getAllPlaneQuery) {
			
			return genericNoSqlReadRepositoryPort.findAll(				
					getAllPlaneQuery.page(),
					getAllPlaneQuery.size()
					);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
		
		}
		
	}
	
}
