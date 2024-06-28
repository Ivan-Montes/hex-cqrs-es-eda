package dev.ime.infrastructure.adapter;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.ime.config.FlightMapper;
import dev.ime.domain.model.Flight;
import dev.ime.infrastructure.entity.FlightMongoEntity;
import dev.ime.infrastructure.repository.read.FlightNoSqlReadRepository;

@ExtendWith(MockitoExtension.class)
class FlightNoSqlReadRepositoryAdapterTest {

	@Mock
	private FlightNoSqlReadRepository flightNoSqlReadRepository;
	
	@Mock
	private FlightMapper flightMapper;

	@InjectMocks
	private FlightNoSqlReadRepositoryAdapter flightNoSqlReadRepositoryAdapter;

	private List<FlightMongoEntity> flightMongoEntityList;
	private List<Flight> flightList;
	private FlightMongoEntity flightMongoEntityTest;
	private Flight flightTest;
	private final ObjectId objectId = ObjectId.get();
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
		
		flightMongoEntityTest = new FlightMongoEntity(
				objectId,
				flightId,
				origin,
				destiny,
				departureDate,
				departureTime,
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
	void FlightNoSqlReadRepositoryAdapter_findAll_ReturnListFlight() {
		
		flightMongoEntityList.add(flightMongoEntityTest);
		flightList.add(flightTest);
		@SuppressWarnings("unchecked")
		Page<FlightMongoEntity>pageMock = Mockito.mock(Page.class);
		Mockito.when(flightNoSqlReadRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(pageMock);		
		Mockito.when(pageMock.toList()).thenReturn(flightMongoEntityList);

		Mockito.when(flightMapper.fromListMongoToListDomain(Mockito.anyList())).thenReturn(flightList);
		
		List<Flight> list = flightNoSqlReadRepositoryAdapter.findAll(0,1);
		
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
	void FlightNoSqlReadRepositoryAdapter_findById_OptionalFlight() {
		
		Mockito.when(flightNoSqlReadRepository.findFirstByFlightId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(flightMongoEntityTest));
		Mockito.when(flightMapper.fromMongoToDomain(Mockito.any(FlightMongoEntity.class))).thenReturn(flightTest);
		
		Optional<Flight> optFlight = flightNoSqlReadRepositoryAdapter.findById(flightId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optFlight).isNotNull(),
				()-> Assertions.assertThat(optFlight.get().getFlightId()).isEqualTo(flightId),
				()-> Assertions.assertThat(optFlight.get().getOrigin()).isEqualTo(origin),
				()-> Assertions.assertThat(optFlight.get().getDestiny()).isEqualTo(destiny),
				()-> Assertions.assertThat(optFlight.get().getDepartureDate()).isEqualTo(departureDate),
				()-> Assertions.assertThat(optFlight.get().getDepartureTime()).isEqualTo(departureTime),
				()-> Assertions.assertThat(optFlight.get().getPlaneId()).isEqualTo(planeId)
				);	
	}
	
}
