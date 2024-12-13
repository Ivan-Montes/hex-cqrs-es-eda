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
import dev.ime.application.dto.PlaneDto;
import dev.ime.application.event.PlaneCreatedEvent;
import dev.ime.application.event.PlaneDeletedEvent;
import dev.ime.application.event.PlaneUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.port.inbound.GenericCommandServicePort;

@WebMvcTest(PlaneCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlaneCommandControllerTest {

	@MockitoBean
	private GenericCommandServicePort<PlaneDto> genericCommandServicePort;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockitoBean
	private LoggerUtil loggerUtil;	

	private static final String PATH = "/api/planes";	
	private PlaneDto planeDtoTest;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	private final Long databaseSequence = 27L;
	
	@BeforeEach
	private void createObjects() {
		
		planeDtoTest = new PlaneDto(
				planeId,
				name,
				capacity
				);
	}
	
	@Test
	void PlaneCommandController_create_ReturnResponseEntityEvent() throws Exception {

		PlaneCreatedEvent event = new PlaneCreatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		Mockito.when(genericCommandServicePort.create(Mockito.any(PlaneDto.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(planeDtoTest)))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(name)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.capacity", org.hamcrest.Matchers.equalTo(capacity)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_PLANE)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.PLANE_CREATED)))
		;
	}

	@Test
	void PlaneCommandController_update_ReturnResponseEntityEvent() throws Exception {

		PlaneUpdatedEvent event = new PlaneUpdatedEvent(
				databaseSequence,
				planeId,
				name,
				capacity
				);
		Mockito.when(genericCommandServicePort.update(Mockito.any(UUID.class), Mockito.any(PlaneDto.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}", planeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(planeDtoTest)))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(name)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.capacity", org.hamcrest.Matchers.equalTo(capacity)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_PLANE)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.PLANE_UPDATED)))
		;
	}

	@Test
	void PlaneCommandController_deleteById_ReturnResponseEntityEvent() throws Exception {
		
		PlaneDeletedEvent event = new PlaneDeletedEvent(
				databaseSequence,
				planeId
				);
		Mockito.when(genericCommandServicePort.deleteById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}", planeId))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_PLANE)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.PLANE_DELETED)))
		;
	}

	@Test
	void PlaneCommandController_deleteById_ReturnResponseEntityEventNull() throws Exception {
		
		Mockito.when(genericCommandServicePort.deleteById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}", planeId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
		;
	}
	
}
