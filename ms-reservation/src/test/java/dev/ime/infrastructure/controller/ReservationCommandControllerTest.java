package dev.ime.infrastructure.controller;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dto.ReservationDto;
import dev.ime.application.event.ReservationCreatedEvent;
import dev.ime.application.event.ReservationDeletedEvent;
import dev.ime.application.event.ReservationUpdatedEvent;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.port.inbound.GenericCommandServicePort;

@WebMvcTest(ReservationCommandController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservationCommandControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private GenericCommandServicePort<ReservationDto> genericCommandServicePort;

	@Autowired
    private ObjectMapper objectMapper;
	
	@MockBean
	private LoggerUtil loggerUtil;

	private final String PATH = "/api/reservations";

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet;
	private final UUID seatId = UUID.randomUUID();
	private final Long databaseSequence = 37L;
	private ReservationDto reservationDtoTest;

	@BeforeEach
	private void createObjects() {
		
		seatIdsSet = new HashSet<>();
		seatIdsSet.add(seatId);
		reservationDtoTest = new ReservationDto(reservationId, clientId, flightId, seatIdsSet);
		
	}
		
	@Test
	void ReservationCommandController_create_ReturnResponseEntityEvent() throws Exception {

		ReservationCreatedEvent event = new ReservationCreatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.when(genericCommandServicePort.create(Mockito.any(ReservationDto.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.post(PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reservationDtoTest)))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.reservationId", org.hamcrest.Matchers.equalTo(reservationId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.clientId", org.hamcrest.Matchers.equalTo(clientId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.flightId", org.hamcrest.Matchers.equalTo(flightId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_RESERV)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.RESERVATION_CREATED)))
		;
		
	}

	@Test
	void ReservationCommandController_update_ReturnResponseEntityEvent() throws Exception {

		ReservationUpdatedEvent event = new ReservationUpdatedEvent(
				databaseSequence,
				reservationId,
				clientId,
				flightId,
				seatIdsSet
				);
		Mockito.when(genericCommandServicePort.update(Mockito.any(UUID.class), Mockito.any(ReservationDto.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/{id}", reservationId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reservationDtoTest)))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.reservationId", org.hamcrest.Matchers.equalTo(reservationId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.clientId", org.hamcrest.Matchers.equalTo(clientId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.flightId", org.hamcrest.Matchers.equalTo(flightId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_RESERV)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.RESERVATION_UPDATED)))
		;
		
	}

	@Test
	void ReservationCommandController_delete_ReturnResponseEntityEvent() throws Exception {

		ReservationDeletedEvent event = new ReservationDeletedEvent(databaseSequence, reservationId);

		Mockito.when(genericCommandServicePort.deleteById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(event));
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}", reservationId))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.reservationId", org.hamcrest.Matchers.equalTo(reservationId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventCategory", org.hamcrest.Matchers.equalTo(ApplicationConstant.CAT_RESERV)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.eventType", org.hamcrest.Matchers.equalTo(ApplicationConstant.RESERVATION_DELETED)))
		;
		
	}

	@Test
	void ReservationCommandController_deleteById_ReturnResponseEntityEventNull()  throws Exception {
		
		Mockito.when(genericCommandServicePort.deleteById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.delete(PATH + "/{id}", reservationId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
		;
	}

}
