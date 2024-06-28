package dev.ime.infrastructure.controller;


import java.time.LocalDate;
import java.time.LocalTime;
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
import dev.ime.application.dto.FlightDto;
import dev.ime.config.FlightMapper;
import dev.ime.config.LoggerUtil;
import dev.ime.domain.model.Flight;
import dev.ime.domain.port.inbound.FlightQueryServicePort;

@WebMvcTest(FlightQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class FlightQueryControllerTest {

	@MockBean
	private FlightQueryServicePort flightQueryServicePort;

	@MockBean
	private FlightMapper flightMapper;	

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private LoggerUtil loggerUtil;
	
	private final String PATH = "/api/flights";	
	private List<FlightDto> flightDtoList;
	private List<Flight> flightList;
	private FlightDto flightDtoTest;
	private Flight flightTest;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {
		
		flightDtoList = new ArrayList<>();		
		flightList = new ArrayList<>();
		
		flightDtoTest = new FlightDto(
				flightId,
				origin,
				destiny,
				departureDate.toString(),
				departureTime.toString(),
				planeId
				);	
		
		flightTest = new Flight();
		flightTest.setFlightId(flightId);
		flightTest.setOrigin(origin);
		flightTest.setDestiny(destiny);
		flightTest.setDepartureDate(departureDate);
		flightTest.setDepartureTime(departureTime);
		flightTest.setPlaneId(planeId);
		
	}
	
	
	@Test
	void FlightQueryController_getAll_ReturnResponseListFlightDto() throws Exception {
		
		flightDtoList.add(flightDtoTest);
		flightList.add(flightTest);
		Mockito.when(flightQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(flightList);
		Mockito.when(flightMapper.fromListDomainToListDto(Mockito.anyList())).thenReturn(flightDtoList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].flightId", org.hamcrest.Matchers.equalTo(flightId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].origin", org.hamcrest.Matchers.equalTo(origin)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].destiny", org.hamcrest.Matchers.equalTo(destiny)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].departureDate", org.hamcrest.Matchers.equalTo(departureDate.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].departureTime", org.hamcrest.Matchers.equalTo(departureTime.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		;
	}

	@Test
	void FlightQueryController_getAll_WithBadParameters_ReturnResponseListFlightDtoEmtpy() throws Exception {
		
		Mockito.when(flightQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(flightList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("size", "-1")
				.param("page", "-1")
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void FlightQueryController_getAll_WithRightsParameters_ReturnResponseListFlightDtoEmtpy() throws Exception {
		
		Mockito.when(flightQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(flightList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("size", "2")
				.param("page", "2")
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void FlightQueryController_getById_ReturnResponseFlightDto() throws Exception {		

		Mockito.when(flightQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(flightTest));
		Mockito.when(flightMapper.fromDomainToDto(Mockito.any())).thenReturn(flightDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", flightId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.flightId", org.hamcrest.Matchers.equalTo(flightId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.origin", org.hamcrest.Matchers.equalTo(origin)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.destiny", org.hamcrest.Matchers.equalTo(destiny)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.departureDate", org.hamcrest.Matchers.equalTo(departureDate.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.departureTime", org.hamcrest.Matchers.equalTo(departureTime.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		;
	}

	@Test
	void FlightQueryController_getById_ReturnResponseFlightDtoEmpty() throws Exception {
		
		Mockito.when(flightQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", flightId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.origin", org.hamcrest.Matchers.equalTo(ApplicationConstant.NODATA)))
		;
	}
	
}
