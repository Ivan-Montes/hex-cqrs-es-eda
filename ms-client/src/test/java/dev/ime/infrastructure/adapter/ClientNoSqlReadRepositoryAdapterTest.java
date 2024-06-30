package dev.ime.infrastructure.adapter;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.ime.config.ClientMapper;
import dev.ime.domain.model.Client;
import dev.ime.infrastructure.entity.ClientMongoEntity;
import dev.ime.infrastructure.repository.read.ClientNoSqlReadRepository;

@ExtendWith(MockitoExtension.class)
class ClientNoSqlReadRepositoryAdapterTest {

	@Mock
	private ClientNoSqlReadRepository clientNoSqlReadRepository;

	@Mock
	private ClientMapper clientMapper;

	@InjectMocks
	private ClientNoSqlReadRepositoryAdapter clientNoSqlReadRepositoryAdapter;

	private ClientMongoEntity mongoEntity;
	private Client domainEntity;
	private List<ClientMongoEntity> mongoEntityList;
	private List<Client> domainEntityList;
	private final ObjectId objectId = ObjectId.get();
	private final UUID id = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	
	@BeforeEach
	private void createObjects() {
		
		mongoEntity = new ClientMongoEntity();
		mongoEntity.setMongoId(objectId);
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
	void ClientNoSqlReadRepositoryAdapter_findAll_ReturnListClient() {
		
		@SuppressWarnings("unchecked")
		Page<ClientMongoEntity> pageMock = Mockito.mock(Page.class);
		mongoEntityList.add(mongoEntity);
		domainEntityList.add(domainEntity);
		Mockito.when(clientNoSqlReadRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(pageMock);
		Mockito.when(pageMock.toList()).thenReturn(mongoEntityList);
		Mockito.when(clientMapper.fromListMongoToListDomain(Mockito.anyList())).thenReturn(domainEntityList);
		
		List<Client> list = clientNoSqlReadRepositoryAdapter.findAll(0,1);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getClientId()).isEqualTo(id),
				()-> Assertions.assertThat(list.get(0).getName()).isEqualTo(name),
				()-> Assertions.assertThat(list.get(0).getLastname()).isEqualTo(lastname)
				);	
	}

	@Test
	void ClientNoSqlReadRepositoryAdapter_findById_ReturnOptionalClient() {
		
		Mockito.when(clientNoSqlReadRepository.findFirstByClientId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(mongoEntity));
		Mockito.when(clientMapper.fromMongoToDomain(Mockito.any(ClientMongoEntity.class))).thenReturn(domainEntity);
		
		Optional<Client> optClient = clientNoSqlReadRepositoryAdapter.findById(id);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optClient).isNotNull(),
				()-> Assertions.assertThat(optClient).isNotEmpty(),
				()-> Assertions.assertThat(optClient.get().getClientId()).isEqualTo(id),
				()-> Assertions.assertThat(optClient.get().getName()).isEqualTo(name),
				()-> Assertions.assertThat(optClient.get().getLastname()).isEqualTo(lastname)
				);	
	}
	
}
