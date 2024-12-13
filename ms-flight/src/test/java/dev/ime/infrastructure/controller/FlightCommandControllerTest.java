package dev.ime.infrastructure.controller;


import java.time.LocalDate;
import java.time.LocalTime;
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
import dev.ime.application.dto.FlightDto;
import dev.ime.application.event.FlightCreatedEvent;
import dev.ime.application.event.FlightDeletedEvent;
import dev.ime.application.event.FlightUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.port.inbound.FlightCommandServicePort;

@WebMvcTest(FlightCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class FlightCommandControllerTest {

	@MockitoBean
	private FlightCommandServicePort<FlightDto> flightCommandServicePort;	

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@MockitoBean
	private LoggerUtil loggerUtil;	

	private static final String PATH = "/api/flights";	
	private FlightDto flightDtoTest;
	private final Long databaseSequence = 11L;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {
		
		flightDtoTest = new FlightDto(
				flightId,
				origin,
				destiny,
				departureDate.toString(),
				departureTime.toString(),
				planeId
				);
	}
	
	
	@Test
	void FlightCommandController_create_ReturnResponseEntityEvent() throws Exception {
		
		FlightCreatedEvent event = new FlightCreatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId);
		Mockito.when(flightCommandServicePort.create(Mockito.any(FlightDto.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(flightDtoTest)))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.flightId", org.hamcrest.Matchers.equalTo(flightId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.origin", org.hamcrest.Matchers.equalTo(origin)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.destiny", org.hamcrest.Matchers.equalTo(destiny)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.departureDate", org.hamcrest.Matchers.equalTo(departureDate.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.departureTime", org.hamcrest.Matchers.equalTo(departureTime.toString() + ":00")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_FLIGHT)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.FLIGHT_CREATED)))
		;
	}

	@Test
	void FlightCommandController_update_ReturnResponseEntityEvent() throws Exception {
		
		FlightUpdatedEvent event = new FlightUpdatedEvent(
				databaseSequence,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
				planeId
				);
		Mockito.when(flightCommandServicePort.update(Mockito.any(UUID.class), Mockito.any(FlightDto.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}", flightId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(flightDtoTest)))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.flightId", org.hamcrest.Matchers.equalTo(flightId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.origin", org.hamcrest.Matchers.equalTo(origin)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.destiny", org.hamcrest.Matchers.equalTo(destiny)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.departureDate", org.hamcrest.Matchers.equalTo(departureDate.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.departureTime", org.hamcrest.Matchers.equalTo(departureTime.toString() + ":00")))
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_FLIGHT)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.FLIGHT_UPDATED)))
		;
	}

	@Test
	void FlightCommandController_deleteById_ReturnResponseEntityEvent() throws Exception {
		
		FlightDeletedEvent event = new FlightDeletedEvent(
				databaseSequence,
				flightId
		);
		Mockito.when(flightCommandServicePort.deleteById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}", flightId))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.flightId", org.hamcrest.Matchers.equalTo(flightId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_FLIGHT)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.FLIGHT_DELETED)))
		;
		
	}

	@Test
	void FlightCommandController_create_ReturnResponseEntityEventNull() throws Exception {
		
		Mockito.when(flightCommandServicePort.deleteById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}", flightId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
		;
		
	}

}
