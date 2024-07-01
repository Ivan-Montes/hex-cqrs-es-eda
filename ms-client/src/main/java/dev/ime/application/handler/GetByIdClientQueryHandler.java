package dev.ime.application.handler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.exception.ResourceNotFoundException;
import dev.ime.application.usecase.GetByIdClientQuery;
import dev.ime.domain.model.Client;
import dev.ime.domain.port.outbound.ClientNoSqlReadRepositoryPort;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@Component
public class GetByIdClientQueryHandler implements QueryHandler<Optional<Client>> {

	private final ClientNoSqlReadRepositoryPort clientNoSqlReadRepositoryPort;
	
	public GetByIdClientQueryHandler(ClientNoSqlReadRepositoryPort clientNoSqlReadRepositoryPort) {
		super();
		this.clientNoSqlReadRepositoryPort = clientNoSqlReadRepositoryPort;
	}

	@Override
	public Optional<Client> handle(Query query) {
		
		if ( query instanceof GetByIdClientQuery getByIdQuery) {
			
			UUID clientId = getByIdQuery.clientId();
			Client client = validateClientExists(clientId);
			
			return Optional.ofNullable(client);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
			
		}
		
	}

	private Client validateClientExists(UUID clientId) {
		
		Optional<Client> optClient = clientNoSqlReadRepositoryPort.findById(clientId);
		
		if ( optClient.isEmpty() ) {
			 
			throw new ResourceNotFoundException(Map.of(ApplicationConstant.CLIENTID,String.valueOf(clientId)));
		}
		
		return optClient.get();
	}
	
}
