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
import dev.ime.application.dto.SeatDto;
import dev.ime.application.event.SeatCreatedEvent;
import dev.ime.application.event.SeatDeletedEvent;
import dev.ime.application.event.SeatUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.port.inbound.GenericCommandServicePort;

@WebMvcTest(SeatCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class SeatCommandControllerTest {

	@MockitoBean
	private GenericCommandServicePort<SeatDto> genericCommandServicePort;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockitoBean
	private LoggerUtil loggerUtil;	

	private static final String PATH = "/api/seats";	
	private SeatDto seatDtoTest;
	private final Long databaseSequence = 27L;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {
		
		seatDtoTest = new SeatDto(
				seatId,
				seatNumber,
				planeId
				);
	}
		
	@Test
	void SeatCommandController_create_ReturnResponseEntityEvent() throws Exception {

		SeatCreatedEvent event = new SeatCreatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		Mockito.when(genericCommandServicePort.create(Mockito.any(SeatDto.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(seatDtoTest)))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.seatId", org.hamcrest.Matchers.equalTo(seatId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.seatNumber", org.hamcrest.Matchers.equalTo(seatNumber)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_SEAT)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.SEAT_CREATED)))
		;
	}
	
	@Test
	void SeatCommandController_update_ReturnResponseEntityEvent() throws Exception {
		
		SeatUpdatedEvent event = new SeatUpdatedEvent(
				databaseSequence,
				seatId,
				seatNumber,
				planeId
				);
		Mockito.when(genericCommandServicePort.update(Mockito.any(UUID.class), Mockito.any(SeatDto.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}", seatId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(seatDtoTest)))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.seatId", org.hamcrest.Matchers.equalTo(seatId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.seatNumber", org.hamcrest.Matchers.equalTo(seatNumber)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_SEAT)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.SEAT_UPDATED)))
		;
	}

	@Test
	void SeatCommandController_deleteById_ReturnResponseEntityEvent() throws Exception {

		SeatDeletedEvent event = new SeatDeletedEvent(
				databaseSequence,
				seatId
				);
		Mockito.when(genericCommandServicePort.deleteById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}", seatId))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.seatId", org.hamcrest.Matchers.equalTo(seatId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_SEAT)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.SEAT_DELETED)))
		;
	}
	

	@Test
	void SeatCommandController_deleteById_ReturnResponseEntityEventNull() throws Exception {
		
	Mockito.when(genericCommandServicePort.deleteById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}", seatId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
		;
	}
	
}
