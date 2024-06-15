package dev.ime.config;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.dto.ClientDto;
import dev.ime.domain.model.Client;
import dev.ime.infrastructure.entity.ClientMongoEntity;


@ExtendWith(MockitoExtension.class)
class ClientMapperTest {
	
	@InjectMocks
	private ClientMapper clientMapper;
	
	private ClientMongoEntity mongoEntity;
	private Client domainEntity;
	private List<ClientMongoEntity> mongoEntityList;
	private List<Client> domainEntityList;
	private final UUID id = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	
	@BeforeEach
	private void createObjects() {
		
		mongoEntity = new ClientMongoEntity();
		mongoEntity.setClientId(id);
		mongoEntity.setName(name);
		mongoEntity.setLastname(lastname);
		
		domainEntity = new Client();
		domainEntity.setClientId(id);
		domainEntity.setName(name);
		domainEntity.setLastname(lastname);
		
		mongoEntityList = new ArrayList<>();
		domainEntityList = new ArrayList<>();
		
	}		
	
	@Test
	void ClientMapper_fromMongoToDomain_ReturnClient() {
		
		Client client = clientMapper.fromMongoToDomain(mongoEntity);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(client).isNotNull(),
				()-> Assertions.assertThat(client.getClientId()).isEqualTo(id),
				()-> Assertions.assertThat(client.getName()).isEqualTo(name),
				()-> Assertions.assertThat(client.getLastname()).isEqualTo(lastname)
				);	
	}

	@Test
	void ClientMapper_fromListMongoToDomain_ReturnListClient() {
		
		mongoEntityList.add(mongoEntity);
		
		List<Client> list = clientMapper.fromListMongoToListDomain(mongoEntityList);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getClientId()).isEqualTo(id),
				()-> Assertions.assertThat(list.get(0).getName()).isEqualTo(name),
				()-> Assertions.assertThat(list.get(0).getLastname()).isEqualTo(lastname)
				);
				
	}

	@Test
	void ClientMapper_fromListMongoToDomain_ReturnListClientNull() {
		
		List<Client> list = clientMapper.fromListMongoToListDomain(null);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);
				
	}
	
	@Test
	void ClientMapper_fromDomainToDto_ReturnClientDto() {
		
		ClientDto clientDto = clientMapper.fromDomainToDto(domainEntity);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(clientDto).isNotNull(),
				()-> Assertions.assertThat(clientDto.clientId()).isEqualTo(id),
				()-> Assertions.assertThat(clientDto.name()).isEqualTo(name),
				()-> Assertions.assertThat(clientDto.lastname()).isEqualTo(lastname)
				);	
	}	

	@Test
	void ClientMapper_fromListDomainToDto_ReturnListClientDto() {
		
		domainEntityList.add(domainEntity);
		
		List<ClientDto> list = clientMapper.fromListDomainToListDto(domainEntityList);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).clientId()).isEqualTo(id),
				()-> Assertions.assertThat(list.get(0).name()).isEqualTo(name),
				()-> Assertions.assertThat(list.get(0).lastname()).isEqualTo(lastname)
				);	
	}

	@Test
	void ClientMapper_fromListDomainToDto_ReturnListClientDtoNull() {
		
		List<ClientDto> list = clientMapper.fromListDomainToListDto(null);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);
	}
	
}
