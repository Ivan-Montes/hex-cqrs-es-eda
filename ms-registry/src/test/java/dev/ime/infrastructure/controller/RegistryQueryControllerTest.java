package dev.ime.infrastructure.controller;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dto.RegistryDto;
import dev.ime.config.LoggerUtil;
import dev.ime.config.RegistryMapper;
import dev.ime.domain.model.Registry;
import dev.ime.domain.port.inbound.GenericQueryServicePort;

@WebMvcTest(RegistryQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistryQueryControllerTest {

	@MockBean
	private GenericQueryServicePort<Registry> genericQueryServicePort;
	
	@MockBean
	private RegistryMapper registryMapper;

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private LoggerUtil loggerUtil;

	private final String PATH = "/api/registries";

	private final UUID registryId = UUID.randomUUID();
	private final Long sequence = 73L;
	private Instant timeInstant;
	private Map<String, Object> evenDataMap;
	private Registry registryTest;
	private RegistryDto registryDtoTest;
	private List<Registry> registryList;
	private List<RegistryDto> registryDtoList;

	@BeforeEach
	private void createObjects() {

		timeInstant = Instant.now();
		
		registryList = new ArrayList<>();
		registryDtoList = new ArrayList<>();
		
		registryTest = new Registry(
				registryId,
				ApplicationConstant.CAT_REGISTRY,
				ApplicationConstant.REGISTRY_CREATED,
				timeInstant,
				sequence,
				evenDataMap
				);
		
		registryDtoTest = new RegistryDto(
				registryId,
				ApplicationConstant.CAT_REGISTRY,
				ApplicationConstant.REGISTRY_CREATED,
				timeInstant,
				sequence,
				evenDataMap
				);
		
		evenDataMap = new HashMap<>();
	}
	
	@Test
	void RegistryQueryController_getAll_ReturnResponseListDto() throws Exception {

		registryDtoList.add(registryDtoTest);
		registryList.add(registryTest);
		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(registryList);
		Mockito.when(registryMapper.fromListDomainToListDto(Mockito.anyList())).thenReturn(registryDtoList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].eventId", org.hamcrest.Matchers.equalTo(registryId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_REGISTRY)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.REGISTRY_CREATED)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].timeInstant", org.hamcrest.Matchers.equalTo(timeInstant.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].sequence", org.hamcrest.Matchers.equalTo(sequence.intValue())))

		;
	}

	@Test
	void RegistryQueryController_getAll_WithBadParams_ReturnResponseListDtoEmpty() throws Exception {

		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(registryList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "-1")
				.param("size", "-1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void RegistryQueryController_getAll_WithRightParams_ReturnResponseListDtoEmpty() throws Exception {

		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(registryList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "2")
				.param("size", "2"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void RegistryQueryController_getById_ReturnResponseDto() throws Exception {
		
		Mockito.when(genericQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(registryTest));
		Mockito.when(registryMapper.fromDomainToDto(Mockito.any(Registry.class))).thenReturn(registryDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", registryId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventId", org.hamcrest.Matchers.equalTo(registryId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_REGISTRY)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.REGISTRY_CREATED)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.timeInstant", org.hamcrest.Matchers.equalTo(timeInstant.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.sequence", org.hamcrest.Matchers.equalTo(sequence.intValue())))
		;
	}

	@Test
	void RegistryQueryController_getById_ReturnResponseDtoEmpty() throws Exception {
		
		Mockito.when(genericQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", registryId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventId", org.hamcrest.Matchers.equalTo(ApplicationConstant.ZEROUUID)))
		;
	}	

}
