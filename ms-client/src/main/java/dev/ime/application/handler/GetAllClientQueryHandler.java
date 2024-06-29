package dev.ime.application.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.usecase.GetAllClientQuery;
import dev.ime.domain.model.Client;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;
import dev.ime.domain.port.outbound.ClientNoSqlReadRepositoryPort;

@Component
public class GetAllClientQueryHandler implements QueryHandler<List<Client>>{

	private final ClientNoSqlReadRepositoryPort clientNoSqlReadRepositoryPort;

	public GetAllClientQueryHandler(ClientNoSqlReadRepositoryPort clientNoSqlReadRepositoryPort) {
		super();
		this.clientNoSqlReadRepositoryPort = clientNoSqlReadRepositoryPort;
	}

	@Override
	public List<Client> handle(Query query) {
		
		if (query instanceof GetAllClientQuery getAllClientQuery) {
			
			return clientNoSqlReadRepositoryPort.findAll(					
					getAllClientQuery.page(),
					getAllClientQuery.size()
			);
			
		}else {
			
			throw new IllegalArgumentException(ApplicationConstant.MSG_ILLEGAL_QUERY);
		
		}
		
	}

}
