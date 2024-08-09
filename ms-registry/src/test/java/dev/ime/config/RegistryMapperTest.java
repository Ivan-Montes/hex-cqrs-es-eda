package dev.ime.config;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dto.RegistryDto;
import dev.ime.domain.model.Registry;
import dev.ime.infrastructure.entity.RegistryJpaEntity;

@ExtendWith(MockitoExtension.class)
class RegistryMapperTest {

	@Mock
	private ObjectMapper objectMapper;	
	
	@Mock
	private LoggerUtil loggerUtil;	
	
	@InjectMocks
	private RegistryMapper registryMapper;
	
	private class JsonExceptionMod extends JsonProcessingException{

		private static final long serialVersionUID = 8680233791195124753L;

		protected JsonExceptionMod(String msg) {
			super(msg);			
		}};	

	private final UUID registryId = UUID.randomUUID();
	private final Long sequence = 73L;
	private Instant timeInstant;
	private String eventDataStr = "";
	private RegistryJpaEntity registryJpaEntityTest;
	private Registry registryTest;
	private List<RegistryJpaEntity> registryJpaEntityList;
	private List<Registry> registryList;
	private Map<String, Object> evenDataMap;
	
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
				new HashMap<>()
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
	
	@SuppressWarnings("unchecked")
	@Test
	void RegistryMapper_fromJpaToDomain_ReturnRegistry() throws JsonProcessingException {
		
		Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(evenDataMap);
		
		Registry registry = registryMapper.fromJpaToDomain(registryJpaEntityTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(registry).isNotNull(),
				()-> Assertions.assertThat(registry.getEventId()).isEqualTo(registryId),
				()-> Assertions.assertThat(registry.getEventCategory()).isEqualTo(ApplicationConstant.CAT_REGISTRY),
				()-> Assertions.assertThat(registry.getEventType()).isEqualTo(ApplicationConstant.REGISTRY_CREATED),
				()-> Assertions.assertThat(registry.getTimeInstant()).isEqualTo(timeInstant),
				()-> Assertions.assertThat(registry.getSequence()).isEqualTo(sequence)
				);		
	}

	@Test
	void RegistryMapper_fromListJpaToListDomain_ReturnListRegistry() {
		
		registryJpaEntityList.add(registryJpaEntityTest);
		
		List<Registry> list = registryMapper.fromListJpaToListDomain(registryJpaEntityList);
		
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
//
	@SuppressWarnings("unchecked")
	@Test
	void RegistryMapper_fromJpaToDomain_ReturnListRegistryWithException() throws JsonProcessingException {

		registryJpaEntityList.add(registryJpaEntityTest);
		Mockito.when(objectMapper.readValue(Mockito.anyString(), Mockito.any(TypeReference.class))).thenThrow(new JsonExceptionMod("OMG exception"));

		List<Registry> list = registryMapper.fromListJpaToListDomain(registryJpaEntityList);
		
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
	void RegistryMapper_fromListJpaToListDomain_ReturnListRegistryEmpty() {
				
		List<Registry> list = registryMapper.fromListJpaToListDomain(null);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);	
	}	
	
	@Test
	void RegistryMapper_fromDomainToDto_ReturnDto() {
		
		RegistryDto dto = registryMapper.fromDomainToDto(registryTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(dto).isNotNull(),
				()-> Assertions.assertThat(dto.eventId()).isEqualTo(registryId),
				()-> Assertions.assertThat(dto.eventCategory()).isEqualTo(ApplicationConstant.CAT_REGISTRY),
				()-> Assertions.assertThat(dto.eventType()).isEqualTo(ApplicationConstant.REGISTRY_CREATED),
				()-> Assertions.assertThat(dto.timeInstant()).isEqualTo(timeInstant),
				()-> Assertions.assertThat(dto.sequence()).isEqualTo(sequence)
				);		
	}

	@Test
	void RegistryMapper_fromDomainToDto_ReturnListDto() {
		
		registryList.add(registryTest);
		
		List<RegistryDto> list = registryMapper.fromListDomainToListDto(registryList);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).eventId()).isEqualTo(registryId),
				()-> Assertions.assertThat(list.get(0).eventCategory()).isEqualTo(ApplicationConstant.CAT_REGISTRY),
				()-> Assertions.assertThat(list.get(0).eventType()).isEqualTo(ApplicationConstant.REGISTRY_CREATED),
				()-> Assertions.assertThat(list.get(0).timeInstant()).isEqualTo(timeInstant),
				()-> Assertions.assertThat(list.get(0).sequence()).isEqualTo(sequence)
				);		
	}

	@Test
	void RegistryMapper_fromDomainToDto_ReturnListDtoEmtpy() {
				
		List<RegistryDto> list = registryMapper.fromListDomainToListDto(null);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);	
	}	
	
}
