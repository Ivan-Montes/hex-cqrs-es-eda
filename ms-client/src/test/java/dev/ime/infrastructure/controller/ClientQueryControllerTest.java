package dev.ime.infrastructure.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dto.ClientDto;
import dev.ime.config.ClientMapper;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.model.Client;
import dev.ime.domain.port.inbound.ClientQueryServicePort;

@WebMvcTest(ClientQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientQueryControllerTest {

	@MockitoBean
	private ClientQueryServicePort clientQueryServicePort;

	@MockitoBean
	private ClientMapper clientMapper;

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private LoggerUtil loggerUtil;
	private static final String PATH = "/api/clients";
	private final UUID clientId = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private ClientDto clientDtoTest;
	private Client clientTest;
	private List<ClientDto> clientDtoList;
	private List<Client> clientList;
	
	@BeforeEach
	private void createObjects() {
		
		clientDtoTest = new ClientDto(clientId, name, lastname);
		clientTest = new Client(clientId, name, lastname);
		clientDtoList = new ArrayList<>();
		clientList = new ArrayList<>();
		
	}
	
	
	@Test
	void ClientQueryController_getAll_ReturnResponseListClientDto() throws Exception {
		
		clientList.add(clientTest);
		clientDtoList.add(clientDtoTest);
		Mockito.when(clientQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(clientList);
		Mockito.when(clientMapper.fromListDomainToListDto(Mockito.anyList())).thenReturn(clientDtoList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId", org.hamcrest.Matchers.equalTo(clientId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", org.hamcrest.Matchers.equalTo(name)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname", org.hamcrest.Matchers.equalTo(lastname)))
		;
	}

	@Test
	void ClientQueryController_getAll_WithIncorrectParam_ReturnResponseListClientDtoEmpty() throws Exception {
		
		Mockito.when(clientQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(clientList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "-1")
				.param("size", "-1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void ClientQueryController_getAll_WithCorrectParam_ReturnResponseListClientDtoEmpty() throws Exception {
		
		Mockito.when(clientQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(clientList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "2")
				.param("size", "2"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}
	
	@Test
	void ClientQueryController_getById_ReturnResponseClientDto() throws Exception {
		
		Mockito.when(clientQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(clientTest));
		Mockito.when(clientMapper.fromDomainToDto(Mockito.any())).thenReturn(clientDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", clientId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.clientId", org.hamcrest.Matchers.equalTo(clientId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(name)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.lastname", org.hamcrest.Matchers.equalTo(lastname)))
		;
		
	}

	@Test
	void ClientQueryController_getById_ReturnResponseClientDtoEmpty() throws Exception {
		
		Mockito.when(clientQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", clientId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(ApplicationConstant.NODATA)))
		;
		
	}

}
