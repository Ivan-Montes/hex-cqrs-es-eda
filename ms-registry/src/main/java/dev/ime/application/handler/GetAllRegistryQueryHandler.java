package dev.ime.application.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.usecase.GetAllRegistryQuery;
import dev.ime.domain.model.Registry;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class GetAllRegistryQueryHandler implements QueryHandler<List<Registry>>{

	private final GenericReadRepositoryPort<Registry> genericNoSqlReadRepositoryPort;
	
	public GetAllRegistryQueryHandler(GenericReadRepositoryPort<Registry> genericNoSqlReadRepositoryPort) {
		super();
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
	}

	@Override
	public List<Registry> handle(Query query) {

		if (query instanceof GetAllRegistryQuery getAllRegistryQuery) {
			
			return genericNoSqlReadRepositoryPort.findAll(				
					getAllRegistryQuery.page(),
					getAllRegistryQuery.size()
			);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
		
		}
	}

}
