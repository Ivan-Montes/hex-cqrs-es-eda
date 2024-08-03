package dev.ime.application.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.GetByIdRegistryQuery;
import dev.ime.domain.model.Registry;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class GetByIdRegistryQueryHandler implements QueryHandler<Optional<Registry>>{

	private final GenericReadRepositoryPort<Registry> genericNoSqlReadRepositoryPort;
	
	public GetByIdRegistryQueryHandler(GenericReadRepositoryPort<Registry> genericNoSqlReadRepositoryPort) {
		super();
		this.genericNoSqlReadRepositoryPort = genericNoSqlReadRepositoryPort;
	}

	@Override
	public Optional<Registry> handle(Query query) {
		
		if ( query instanceof GetByIdRegistryQuery getByIdRegistryQuery) {
			
			Registry registry = validateRegistryExists(getByIdRegistryQuery.eventId());
			
			return Optional.ofNullable(registry);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
			
		}
	}

	private Registry validateRegistryExists(UUID eventId) {
		
		Optional<Registry> optRegistry = genericNoSqlReadRepositoryPort.findById(eventId);
		
		if ( optRegistry.isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.REGISTRYID, eventId.toString()));
		}
		
		return optRegistry.get();

	}

}
