package dev.ime.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.dispatch.RegistryQueryDispatcher;
import dev.ime.application.usecase.GetAllRegistryQuery;
import dev.ime.application.usecase.GetByIdRegistryQuery;
import dev.ime.domain.model.Registry;
import dev.ime.domain.port.inbound.GenericQueryServicePort;
import dev.ime.domain.query.QueryHandler;

@Service
public class RegistryQueryService implements GenericQueryServicePort<Registry>{

	private final RegistryQueryDispatcher registryQueryDispatcher;

	public RegistryQueryService(RegistryQueryDispatcher registryQueryDispatcher) {
		super();
		this.registryQueryDispatcher = registryQueryDispatcher;
	}

	@Override
	public List<Registry> getAll(Integer page, Integer size) {
		
		GetAllRegistryQuery getAllQuery = new GetAllRegistryQuery(page, size);
		QueryHandler<List<Registry>> queryHandler = registryQueryDispatcher.getQueryHandler(getAllQuery);
		
		return queryHandler.handle(getAllQuery);
	}

	@Override
	public Optional<Registry> getById(UUID id) {

		GetByIdRegistryQuery getByIdQuery = new GetByIdRegistryQuery(id);
		QueryHandler<Optional<Registry>> queryHandler = registryQueryDispatcher.getQueryHandler(getByIdQuery);
		
		return queryHandler.handle(getByIdQuery);
		
	}
	
	
}
