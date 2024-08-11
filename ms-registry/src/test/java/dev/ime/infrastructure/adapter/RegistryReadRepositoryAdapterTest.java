package dev.ime.infrastructure.adapter;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.config.RegistryMapper;
import dev.ime.domain.model.Registry;
import dev.ime.infrastructure.entity.RegistryJpaEntity;
import dev.ime.infrastructure.repository.RegistryJpaRepository;

@ExtendWith(MockitoExtension.class)
class RegistryReadRepositoryAdapterTest {

	@Mock
	private RegistryJpaRepository registryJpaRepository;
	
	@Mock
	private RegistryMapper registryMapper;
	
	@InjectMocks
	private RegistryReadRepositoryAdapter registryReadRepositoryAdapter;
	
	private final UUID registryId = UUID.randomUUID();
	private final Long sequence = 73L;
	private Instant timeInstant;
	private String eventDataStr = "";
	private Map<String, Object> evenDataMap;
	private Registry registryTest;
	private RegistryJpaEntity registryJpaEntityTest;
	private List<Registry> registryList;
	private List<RegistryJpaEntity> registryJpaEntityList;

	@BeforeEach
	private void createObjects() {
		
		registryJpaEntityList = new ArrayList<>();
		registryList = new ArrayList<>();
		
		registryTest = new Registry(
				registryId,
				ApplicationConstant.CAT_REGISTRY,
				ApplicationConstant.REGISTRY_CREATED,
				timeInstant,
				sequence,
				evenDataMap
				);
		
		registryJpaEntityTest = new RegistryJpaEntity(
				registryId,
				ApplicationConstant.CAT_REGISTRY,
				ApplicationConstant.REGISTRY_CREATED,
				timeInstant,
				sequence,
				eventDataStr
				);
		
		evenDataMap = new HashMap<>();
		
	}
	
	@Test
	void RegistryReadRepositoryAdapter_getAll_ReturnListRegistry() {
		
		registryJpaEntityList.add(registryJpaEntityTest);
		registryList.add(registryTest);
		@SuppressWarnings("unchecked")
		Page<RegistryJpaEntity> pageMock = Mockito.mock(Page.class);
		Mockito.when(registryJpaRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(pageMock);
		Mockito.when(pageMock.toList()).thenReturn(registryJpaEntityList);
		Mockito.when(registryMapper.fromListJpaToListDomain(Mockito.anyList())).thenReturn(registryList);

		List<Registry> list = registryReadRepositoryAdapter.findAll(0,1);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getEventId()).isEqualTo(registryId),
				()-> Assertions.assertThat(list.get(0).getEventCategory()).isEqualTo(ApplicationConstant.CAT_REGISTRY),
				()-> Assertions.assertThat(list.get(0).getEventType()).isEqualTo(ApplicationConstant.REGISTRY_CREATED),
				()-> Assertions.assertThat(list.get(0).getTimeInstant()).isEqualTo(timeInstant),
				()-> Assertions.assertThat(list.get(0).getSequence()).isEqualTo(sequence)
				);
	}

	@Test
	void RegistryReadRepositoryAdapter_getById_ReturnOptionalRegistry() {

		Mockito.when(registryJpaRepository.findById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(registryJpaEntityTest));
		Mockito.when(registryMapper.fromJpaToDomain(Mockito.any(RegistryJpaEntity.class))).thenReturn(registryTest);
		
		Optional<Registry> optRegistry = registryReadRepositoryAdapter.findById(registryId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optRegistry).isNotNull(),
				()-> Assertions.assertThat(optRegistry.get().toString()).hasToString(registryTest.toString()),
				()-> Assertions.assertThat(optRegistry.get().hashCode()).hasSameHashCodeAs(registryTest.hashCode()),
				()-> Assertions.assertThat(optRegistry).contains(registryTest)
				);
	}
	
}
