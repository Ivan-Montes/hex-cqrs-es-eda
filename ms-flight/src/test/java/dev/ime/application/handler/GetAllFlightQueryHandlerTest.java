package dev.ime.application.handler;


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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.usecase.GetAllFlightQuery;
import dev.ime.application.usecase.GetByIdFlightQuery;
import dev.ime.domain.model.Flight;
import dev.ime.domain.port.outbound.FlightNoSqlReadRepositoryPort;

@ExtendWith(MockitoExtension.class)
class GetAllFlightQueryHandlerTest {

	@Mock
	private FlightNoSqlReadRepositoryPort flightNoSqlReadRepositoryPort;

	@InjectMocks
	private GetAllFlightQueryHandler getAllFlightQueryHandler;
	
	private GetAllFlightQuery getAllFlightQuery;
	private GetByIdFlightQuery getByIdFlightQuery;
	private List<Flight> flightList;
	private Flight flightTest;
	private final UUID flightId = UUID.randomUUID();
	private final String origin = "Madrid";
	private final String destiny = "Tokyo";
	private final LocalDate departureDate = LocalDate.parse("2024-04-01");
	private final LocalTime departureTime = LocalTime.parse("15:00");
	private final UUID planeId = UUID.randomUUID();

	@BeforeEach
	private void createObjects() {
		
		getAllFlightQuery = new GetAllFlightQuery(0,1);
		getByIdFlightQuery = new GetByIdFlightQuery( flightId );
		
		flightList = new ArrayList<>();
		
		flightTest = new Flight();
		flightTest.setFlightId(flightId);
		flightTest.setOrigin(origin);
		flightTest.setDestiny(destiny);
		flightTest.setDepartureDate(departureDate);
		flightTest.setDepartureTime(departureTime);
		flightTest.setPlaneId(planeId);
		
	}	

	@Test
	void GetAllFlightQueryHandler_handle_ReturnList() {
		
		flightList.add(flightTest);
		Mockito.when(flightNoSqlReadRepositoryPort.findAll(Mockito.anyInt(), Mockito.anyInt())).thenReturn(flightList);
		
		List<Flight> list = getAllFlightQueryHandler.handle(getAllFlightQuery);
		
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
	void GetAllFlightQueryHandler_handle_ReturnIllegalArgumentException() {		
				
		Exception ex = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()-> getAllFlightQueryHandler.handle(getByIdFlightQuery));
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(ex).isNotNull(),
				()-> Assertions.assertThat(ex.getClass()).isEqualTo(IllegalArgumentException.class)
				);		
	}
	

}
