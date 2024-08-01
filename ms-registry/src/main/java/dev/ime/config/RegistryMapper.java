package dev.ime.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.application.dto.RegistryDto;
import dev.ime.domain.model.Registry;
import dev.ime.infrastructure.entity.RegistryJpaEntity;

@Component
public class RegistryMapper {

	private final ObjectMapper objectMapper;	
	private final LoggerUtil loggerUtil;	

	public RegistryMapper(ObjectMapper objectMapper, LoggerUtil loggerUtil) {
		super();
		this.objectMapper = objectMapper;
		this.loggerUtil = loggerUtil;
	}

	public Registry fromJpaToDomain(RegistryJpaEntity jpaEntity) {
		
		return new Registry(
				jpaEntity.getEventId(),
				jpaEntity.getEventCategory(),
				jpaEntity.getEventType(),
				jpaEntity.getTimeInstant(),
				jpaEntity.getSequence(),
				fromStringToMap(jpaEntity.getEventData())
				);
				
	}

	public List<Registry> fromListJpaToListDomain(List<RegistryJpaEntity> listJpa) {
		
		if ( listJpa == null ) {
			return new ArrayList<>();
		}
		
		return listJpa.stream()
				.map(this::fromJpaToDomain)
				.toList();
	}	
	
	private Map<String, Object> fromStringToMap(String str){
		
		Map<String, Object> map = new HashMap<>();
		
		try {
			
			map =  objectMapper.readValue(str, new TypeReference<Map<String, Object>>() {});
			
		} catch (JsonProcessingException e) {			
			loggerUtil.logInfoAction(getClass().getSimpleName(),"fromStringToMap", e.getLocalizedMessage());
		} 

		return map;	
		
	}
	
	public RegistryDto fromDomainToDto(Registry domainEntity) {
		
		return new RegistryDto(
				domainEntity.getEventId(),
				domainEntity.getEventCategory(),
				domainEntity.getEventType(),
				domainEntity.getTimeInstant(),
				domainEntity.getSequence(),
				domainEntity.getEventData()
				);
	}

	public List<RegistryDto> fromListDomainToListDto(List<Registry> listDomain) {
		
		if ( listDomain == null ) {
			return new ArrayList<>();
		}
		
		return listDomain.stream()
				.map(this::fromDomainToDto)
				.toList();
	}	
	
}
