package dev.ime.application.handler.query;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.query.GetByIdPlaneQuery;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;


@Component
public class GetByIdPlaneQueryHandler implements QueryHandler<Optional<Plane>>{

	private final GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort;

	public GetByIdPlaneQueryHandler(GenericNoSqlReadRepositoryPort<Plane> genericNoSqlReadRepositoryPort) {
		super();
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
	}

	@Override
	public Optional<Plane> handle(Query query) {
		
		if ( query instanceof GetByIdPlaneQuery getByIdPlaneQuery) {
			
			Plane plane = validatePlaneExists(getByIdPlaneQuery.planeId());
			
			return Optional.ofNullable(plane);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
			
		}
	}

	private Plane validatePlaneExists(UUID planeId) {

		Optional<Plane> optPlane = genericNoSqlReadRepositoryPort.findById(planeId);
		
		if ( optPlane.isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.PLANEID,String.valueOf(planeId)));
		}
		
		return optPlane.get();
	}
	
}
