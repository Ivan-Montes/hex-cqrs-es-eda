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
import dev.ime.application.dto.PlaneDto;
import dev.ime.config.LoggerUtil;
import dev.ime.config.PlaneMapper;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.inbound.GenericQueryServicePort;

@WebMvcTest(PlaneQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlaneQueryControllerTest {

	@MockitoBean
	private GenericQueryServicePort<Plane> genericQueryServicePort;
	
	@MockitoBean
	private PlaneMapper planeMapper;

	@Autowired
	private MockMvc mockMvc;	
	
	@MockitoBean
	private LoggerUtil loggerUtil;	

	private static final String PATH = "/api/planes";	
	private List<PlaneDto> planeDtoList;
	private List<Plane> planeList;
	private PlaneDto planeDtoTest;
	private Plane planeTest;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	
	@BeforeEach
	private void createObjects() {
		
		planeDtoList = new ArrayList<>();		
		planeList = new ArrayList<>();	
		
		planeDtoTest = new PlaneDto(
				planeId,
				name,
				capacity
				);
		
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		planeTest.setName(name);
		planeTest.setCapacity(capacity);
		
	}
	
	@Test
	void PlaneQueryController_getAll_ReturnResponseListPlaneDto() throws Exception {
		
		planeList.add(planeTest);
		planeDtoList.add(planeDtoTest);
		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(planeList);
		Mockito.when(planeMapper.fromListDomainToListDto(Mockito.anyList())).thenReturn(planeDtoList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].name", org.hamcrest.Matchers.equalTo(name)))
		.andExpect(MockMvcResultMatchers.jsonPath("$[0].capacity", org.hamcrest.Matchers.equalTo(capacity)))
		;
	}

	@Test
	void PlaneQueryController_getAll_WithBadParam_ReturnResponseListPlaneDtoEmpty() throws Exception {
		
		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(planeList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "-1")
				.param("size", "-1")
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void PlaneQueryController_getAll_WithRightParam_ReturnResponseListPlaneDtoEmpty() throws Exception {
		
		Mockito.when(genericQueryServicePort.getAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(planeList);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH)
				.param("page", "2")
				.param("size", "2")
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$", org.hamcrest.Matchers.empty()))
		;
	}

	@Test
	void PlaneQueryController_getById_ReturnResponsePlaneDto() throws Exception {
	
		Mockito.when(genericQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(planeTest));
		Mockito.when(planeMapper.fromDomainToDto(Mockito.any(Plane.class))).thenReturn(planeDtoTest);
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", planeId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(planeId.toString())))
		.andExpect(MockMvcResultMatchers.jsonPath("$.name", org.hamcrest.Matchers.equalTo(name)))
		.andExpect(MockMvcResultMatchers.jsonPath("$.capacity", org.hamcrest.Matchers.equalTo(capacity)))
		;
	}

	@Test
	void PlaneQueryController_getById_ReturnResponsePlaneDtoEmpty() throws Exception {
	
		Mockito.when(genericQueryServicePort.getById(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{id}", planeId))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$.planeId", org.hamcrest.Matchers.equalTo(ApplicationConstant.ZEROUUID)))
		;
	}
	
}
