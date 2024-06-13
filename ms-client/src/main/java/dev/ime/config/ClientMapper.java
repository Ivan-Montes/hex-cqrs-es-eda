package dev.ime.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import dev.ime.application.dto.ClientDto;
import dev.ime.domain.model.Client;
import dev.ime.infrastructure.entity.ClientMongoEntity;

@Component
public class ClientMapper {

	public ClientMapper() {
		super();
	}

	public Client fromMongoToDomain(ClientMongoEntity mongoEntity) {
		
		Client client = new Client();
		client.setClientId(mongoEntity.getClientId());
		client.setName(mongoEntity.getName());
		client.setLastname(mongoEntity.getLastname());
		
		return client;
	}

	public List<Client> fromListMongoToListDomain(List<ClientMongoEntity> listMongo) {
		
		if ( listMongo == null ) {
			return new ArrayList<>();
		}
		
		return listMongo.stream()
			.map(this::fromMongoToDomain)
			.toList();	
	}
	
	public ClientDto fromDomainToDto(Client domainEntity) {
		
		return new ClientDto(domainEntity.getClientId(), domainEntity.getName(), domainEntity.getLastname());
		
	}

	public List<ClientDto> fromListDomainToListDto(List<Client> listDomain) {
		
		if ( listDomain == null ) {
			return new ArrayList<>();
		}
		
		return listDomain.stream()
				.map(this::fromDomainToDto)
				.toList();
	}
}
