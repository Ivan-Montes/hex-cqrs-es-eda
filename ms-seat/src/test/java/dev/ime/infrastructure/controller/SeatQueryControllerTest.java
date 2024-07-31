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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import dev.ime.application.config.ApplicationConstant;
import dev.ime.application.dto.SeatDto;
import dev.ime.config.LoggerUtil;
import dev.ime.config.SeatMapper;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.inbound.GenericQueryServicePort;

@WebMvcTest(SeatQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class SeatQueryControllerTest {

	@MockBean
	private GenericQueryServicePort<Seat> genericQueryServicePort;
	
	@MockBean
	private SeatMapper seatMapper;

	@Autowired
	private MockMvc mockMvc;	
	
	@MockBean
	private LoggerUtil loggerUtil;	

	private final String PATH = "/api/seats";

	private List<Seat> seatList;
	private List<SeatDto> seatDtoList;
	private Seat seatTest;
	private SeatDto seatDtoTest;
	private Plane planeTest;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;

	@BeforeEach
	private void createObjects() {
		
		seatList = new ArrayList<>();
		seatDtoList = new ArrayList<>();
		
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		planeTest.setName(name);
		planeTest.setCapacity(capacity);
		
		seatTest = new Seat(
				seatId,
				seatNumber,
				planeTest);
		
		seatDtoTest = new SeatDto(
				seatId,
				seatNumber,
				planeId);
	}	

	
	@Test
	void SeatQueryControllerTest_getAll_ReturnResponseListSeatDto() throws Exception {
		
		seatList.add(seatTest);
		seatDtoList.add(seatDtoTest);
		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(seatList);
		Mockito.when(seatMapper.fromListDomainToListDto(Mockito.anyList())).thenReturn(seatDtoList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].seatId", org.hamcrest.Matchers.equalTo(seatId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].seatNumber", org.hamcrest.Matchers.equalTo(seatNumber)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		;
	}

	@Test
	void SeatQueryControllerTest_getAll_WithBadParam_ReturnResponseListSeatDtoEmpty() throws Exception {
		
		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(seatList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "-1")
				.param("size", "-1")
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void SeatQueryControllerTest_getAll_WithRightParam_ReturnResponseListSeatDtoEmpty() throws Exception {
		
		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(seatList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "2")
				.param("size", "2")
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void SeatQueryControllerTest_getById_ReturnResponseSeatDto() throws Exception {
		
		Mockito.when(genericQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(seatTest));
		Mockito.when(seatMapper.fromDomainToDto(Mockito.any(Seat.class))).thenReturn(seatDtoTest);

		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", seatId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.seatId", org.hamcrest.Matchers.equalTo(seatId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.seatNumber", org.hamcrest.Matchers.equalTo(seatNumber)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		;
	}

	@Test
	void SeatQueryControllerTest_getById_ReturnResponseSeatDtoEmtpy() throws Exception {
		
	Mockito.when(genericQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", seatId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.seatId", org.hamcrest.Matchers.equalTo(ApplicationConstant.ZEROUUID)))
		;
	}
	
}
