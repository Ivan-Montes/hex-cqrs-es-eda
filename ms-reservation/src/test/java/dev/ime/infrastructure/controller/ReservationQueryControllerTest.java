package dev.ime.infrastructure.controller;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dto.ReservationDto;
import dev.ime.config.LoggerUtil;
import dev.ime.config.ReservationMapper;
import dev.ime.domain.model.Reservation;
import dev.ime.domain.port.inbound.GenericQueryServicePort;

@WebMvcTest(ReservationQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservationQueryControllerTest {

	@MockBean
	private GenericQueryServicePort<Reservation> genericQueryServicePort;
	
	@MockBean
	private ReservationMapper reservationMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private LoggerUtil loggerUtil;
	
	private final String PATH = "/api/reservations";

	private final UUID reservationId = UUID.randomUUID();
	private final UUID clientId = UUID.randomUUID();
	private final UUID flightId = UUID.randomUUID();
	private Set<UUID> seatIdsSet;
	private final UUID seatId = UUID.randomUUID();
	private ReservationDto reservationDtoTest;
	private Reservation reservationTest;
	private List<ReservationDto> reservationDtoList;
	private List<Reservation> reservationList;

	@BeforeEach
	private void createObjects() {
		
		seatIdsSet = new HashSet<>();
		seatIdsSet.add(seatId);
		reservationDtoTest = new ReservationDto(reservationId, clientId, flightId, seatIdsSet);
		reservationTest = new Reservation(reservationId, clientId, flightId, seatIdsSet);
		reservationDtoList = new ArrayList<>();
		reservationList = new ArrayList<>();
	}
	
	@Test
	void ReservationQueryController_getAll_ReturnResponseListDto() throws Exception {
		
		reservationList.add(reservationTest);
		reservationDtoList.add(reservationDtoTest);
		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(reservationList);
		Mockito.when(reservationMapper.fromListDomainToListDto(Mockito.anyList())).thenReturn(reservationDtoList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].reservationId", org.hamcrest.Matchers.equalTo(reservationId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId", org.hamcrest.Matchers.equalTo(clientId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].flightId", org.hamcrest.Matchers.equalTo(flightId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].seatIdsSet[0]", org.hamcrest.Matchers.equalTo(seatId.toString())))
		;
	}

	@Test
	void ReservationQueryController_getAll_WithBadParams_ReturnResponseListDtoEmpty() throws Exception {

		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(reservationList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "-1")
				.param("size", "-1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void ReservationQueryController_getAll_WithRightParams_ReturnResponseListDtoEmpty() throws Exception {

		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(reservationList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "2")
				.param("size", "2"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void ReservationQueryController_getById_ReturnResponseDto() throws Exception {
		
		Mockito.when(genericQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(reservationTest));
		Mockito.when(reservationMapper.fromDomainToDto(Mockito.any(Reservation.class))).thenReturn(reservationDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", reservationId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.reservationId", org.hamcrest.Matchers.equalTo(reservationId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.clientId", org.hamcrest.Matchers.equalTo(clientId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.flightId", org.hamcrest.Matchers.equalTo(flightId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.seatIdsSet[0]", org.hamcrest.Matchers.equalTo(seatId.toString())))
		;
	}

	@Test
	void ReservationQueryController_getById_ReturnResponseDtoEmpty() throws Exception {
		
		Mockito.when(genericQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", reservationId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.reservationId", org.hamcrest.Matchers.equalTo(ApplicationConstant.ZEROUUID)))
		;
	}
	
}
