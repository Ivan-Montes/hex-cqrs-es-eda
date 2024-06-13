package dev.ime.application.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.ime.application.dispatch.ClientQueryDispatcher;
import dev.ime.application.usecase.GetAllClientQuery;
import dev.ime.application.usecase.GetByIdClientQuery;
import dev.ime.domain.model.Client;
import dev.ime.domain.port.inbound.ClientQueryServicePort;
import dev.ime.domain.query.QueryHandler;

@Service
public class ClientQueryService implements ClientQueryServicePort{

	private final ClientQueryDispatcher clientQueryDispatcher;
	
	public ClientQueryService(ClientQueryDispatcher clientQueryDispatcher) {
		super();
		this.clientQueryDispatcher = clientQueryDispatcher;
	}

	@Override
	public List<Client> getAll() {
		
		GetAllClientQuery getAllQuery = new GetAllClientQuery();
		QueryHandler<List<Client>> queryHandler = clientQueryDispatcher.getQueryHandler(getAllQuery);
		
		return queryHandler.handle(getAllQuery);
	}

	@Override
	public Optional<Client> getById(UUID id) {
		
		GetByIdClientQuery getByIdQuery = new GetByIdClientQuery(id);
		QueryHandler<Optional<Client>> queryHandler = clientQueryDispatcher.getQueryHandler(getByIdQuery);
		
		return queryHandler.handle(getByIdQuery);
	}

}
