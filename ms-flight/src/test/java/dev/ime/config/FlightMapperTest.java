package dev.ime.config;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.dto.FlightDto;
import dev.ime.domain.model.Flight;
import dev.ime.infrastructure.entity.FlightMongoEntity;

@ExtendWith(MockitoExtension.class)
class FlightMapperTest {
	
	@InjectMocks
	private FlightMapper flightMapper;

	private List<FlightMongoEntity> flightMongoEntityList;
	private List<Flight> flightList;
	private FlightMongoEntity flightMongoEntityTest;
	private Flight flightTest;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();
	
	@BeforeEach
	private void createObjects() {
		
		flightMongoEntityList = new ArrayList<>();
		flightList = new ArrayList<>();
		
		flightMongoEntityTest = new FlightMongoEntity();
		flightMongoEntityTest.setFlightId(flightId);
		flightMongoEntityTest.setOrigin(origin);
		flightMongoEntityTest.setDestiny(destiny);
		flightMongoEntityTest.setDepartureDate(departureDate);
		flightMongoEntityTest.setDepartureTime(departureTime);
		flightMongoEntityTest.setPlaneId(planeId);
		
		flightTest = new Flight();
		flightTest.setFlightId(flightId);
		flightTest.setOrigin(origin);
		flightTest.setDestiny(destiny);
		flightTest.setDepartureDate(departureDate);
		flightTest.setDepartureTime(departureTime);
		flightTest.setPlaneId(planeId);		
		
	}
	
	@Test
	void FlightMapper_fromMongoToDomain_ReturnFlight() {
		
		Flight flight = flightMapper.fromMongoToDomain(flightMongoEntityTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(flight).isNotNull(),
				()-> Assertions.assertThat(flight.getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(flight.getOrigin()).isEqualTo(origin),
				()-> Assertions.assertThat(flight.getDestiny()).isEqualTo(destiny),
				()-> Assertions.assertThat(flight.getDepartureDate()).isEqualTo(departureDate),
				()-> Assertions.assertThat(flight.getDepartureTime()).isEqualTo(departureTime),
				()-> Assertions.assertThat(flight.getPlaneId()).isEqualTo(planeId)
				);			
	}

	@Test
	void FlightMapper_fromListMongoToListDomain_ReturnListFlight() {
		
		flightMongoEntityList.add(flightMongoEntityTest);
		
		List<Flight> list = flightMapper.fromListMongoToListDomain(flightMongoEntityList);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(list.get(0).getOrigin()).isEqualTo(origin),
				()-> Assertions.assertThat(list.get(0).getDestiny()).isEqualTo(destiny),
				()-> Assertions.assertThat(list.get(0).getDepartureDate()).isEqualTo(departureDate),
				()-> Assertions.assertThat(list.get(0).getDepartureTime()).isEqualTo(departureTime),
				()-> Assertions.assertThat(list.get(0).getPlaneId()).isEqualTo(planeId)
				);	
	}

	@Test
	void FlightMapper_fromListMongoToListDomain_ReturnListFlightNull() {
				
		List<Flight> list = flightMapper.fromListMongoToListDomain(null);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);	
	}
	
	@Test
	void FlightMapper_fromDomainToDto_ReturnFlightDto() {
		
		FlightDto flightDto = flightMapper.fromDomainToDto(flightTest);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(flightDto).isNotNull(),
				()-> Assertions.assertThat(flightDto.flightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(flightDto.origin()).isEqualTo(origin),
				()-> Assertions.assertThat(flightDto.destiny()).isEqualTo(destiny),
				()-> Assertions.assertThat(flightDto.departureDate()).isEqualTo(departureDate.toString()),
				()-> Assertions.assertThat(flightDto.departureTime()).isEqualTo(departureTime.toString()),
				()-> Assertions.assertThat(flightDto.planeId()).isEqualTo(planeId)
				);	
	}

	@Test
	void FlightMapper_fromListDomainToListDto_ReturnListFlightDto() {
		
		flightList.add(flightTest);
		
		List<FlightDto> list = flightMapper.fromListDomainToListDto(flightList);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).flightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(list.get(0).origin()).isEqualTo(origin),
				()-> Assertions.assertThat(list.get(0).destiny()).isEqualTo(destiny),
				()-> Assertions.assertThat(list.get(0).departureDate()).isEqualTo(departureDate.toString()),
				()-> Assertions.assertThat(list.get(0).departureTime()).isEqualTo(departureTime.toString()),
				()-> Assertions.assertThat(list.get(0).planeId()).isEqualTo(planeId)
				);	
	}

	@Test
	void FlightMapper_fromListDomainToListDto_ReturnListFlightDtoNull() {		
		
		List<FlightDto> list = flightMapper.fromListDomainToListDto(null);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);		
	}
	
}
