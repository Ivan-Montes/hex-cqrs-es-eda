package dev.ime.infrastructure.controller;


import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dto.ClientDto;
import dev.ime.application.event.ClientCreatedEvent;
import dev.ime.application.event.ClientDeletedEvent;
import dev.ime.application.event.ClientUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.port.inbound.ClientCommandServicePort;

@WebMvcTest(ClientCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientCommandControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private ClientCommandServicePort<ClientDto> clientCommandServicePort;

	@Autowired
    private ObjectMapper objectMapper;
	
	@MockitoBean
	private LoggerUtil loggerUtil;
	
	private static final String PATH = "/api/clients";
	private final UUID id = UUID.randomUUID();
	private final String name = "Triss";
	private final String lastname = "Merigold";
	private final Long databaseSequence = 9L;
	private ClientDto clientDtoTest;
	
	@BeforeEach
	private void createObjects() {
		
		clientDtoTest = new ClientDto(id, name, lastname);
		
	}
	
	@Test
	void ClientCommandController_create_ReturnResponseEntityEvent() throws Exception {
		
		ClientCreatedEvent event = new ClientCreatedEvent(databaseSequence, id, name, lastname);
		Mockito.when(clientCommandServicePort.create(Mockito.any(ClientDto.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientDtoTest)))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.clientId", org.hamcrest.Matchers.equalTo(id.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(name)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.lastname", org.hamcrest.Matchers.equalTo(lastname)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_CLIENT)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.CLIENT_CREATED)))
		;
		
	}

	@Test
	void ClientCommandController_update_ReturnResponseEntityEvent()  throws Exception {
		
		ClientUpdatedEvent event = new ClientUpdatedEvent(databaseSequence, id, name, lastname);
		Mockito.when(clientCommandServicePort.update(Mockito.any(UUID.class), Mockito.any(ClientDto.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(clientDtoTest)))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.clientId", org.hamcrest.Matchers.equalTo(id.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(name)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.lastname", org.hamcrest.Matchers.equalTo(lastname)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_CLIENT)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.CLIENT_UPDATED)))
		;
	}

	@Test
	void ClientCommandController_deleteById_ReturnResponseEntityEvent()  throws Exception {
		
		ClientDeletedEvent event = new ClientDeletedEvent(databaseSequence, id);
		Mockito.when(clientCommandServicePort.deleteById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}", id))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.clientId", org.hamcrest.Matchers.equalTo(id.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_CLIENT)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.CLIENT_DELETED)))
		;
	}

	@Test
	void ClientCommandController_deleteById_ReturnResponseEntityEventNull()  throws Exception {
		
		Mockito.when(clientCommandServicePort.deleteById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}", id))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
		;
	}

}
