package dev.ime.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import dev.ime.config.ClientMapper;
import dev.ime.domain.model.Client;
import dev.ime.domain.port.outbound.ClientNoSqlReadRepositoryPort;
import dev.ime.infrastructure.repository.read.ClientNoSqlReadRepository;

@Repository
public class ClientNoSqlReadRepositoryAdapter implements ClientNoSqlReadRepositoryPort{

	private final ClientNoSqlReadRepository clientNoSqlReadRepository;
	private final ClientMapper clientMapper;	
	
	public ClientNoSqlReadRepositoryAdapter(ClientNoSqlReadRepository clientNoSqlReadRepository,
			ClientMapper clientMapper) {
		super();
		this.clientNoSqlReadRepository = clientNoSqlReadRepository;
		this.clientMapper = clientMapper;
	}

	@Override
	public List<Client> findAll() {
		
		return clientMapper.fromListMongoToListDomain( clientNoSqlReadRepository.findAll() );
		
	}

	@Override
	public Optional<Client> findById(UUID id) {
		
		return clientNoSqlReadRepository.findFirstByClientId(id).map(clientMapper::fromMongoToDomain);
		
	}

}
