package dev.ime.config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.ime.application.dto.SeatDto;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.infrastructure.entity.PlaneMongoEntity;
import dev.ime.infrastructure.entity.SeatMongoEntity;

@ExtendWith(MockitoExtension.class)
class SeatMapperTest {

	@InjectMocks
	private SeatMapper seatMapper;
	
	private List<Seat> seatList;
	private Seat seatTest;
	private SeatMongoEntity seatMongoEntityTest;
	private Plane planeTest;
	private PlaneMongoEntity planeMongoEntityTest;
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;	

	@BeforeEach
	private void createObjects() {
		
		seatList = new ArrayList<>();
		
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		planeTest.setName(name);
		planeTest.setCapacity(capacity);
		
		seatTest = new Seat();
		seatTest.setSeatId(seatId);
		seatTest.setSeatNumber(seatNumber);
		seatTest.setPlane(planeTest);
		
		seatMongoEntityTest = new SeatMongoEntity();
		seatMongoEntityTest.setSeatId(seatId);
		seatMongoEntityTest.setSeatNumber(seatNumber);
		seatMongoEntityTest.setPlaneId(planeId);
		
		planeMongoEntityTest = new PlaneMongoEntity();
		planeMongoEntityTest.setPlaneId(planeId);
		planeMongoEntityTest.setName(name);
		planeMongoEntityTest.setCapacity(capacity);
	}
	
	@Test
	void SeatMapper_fromMongoToDomain_ReturnSeat() {
		
		Seat seat = seatMapper.fromMongoToDomain(seatMongoEntityTest, planeMongoEntityTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(seat).isNotNull(),
				()-> Assertions.assertThat(seat.getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(seat.getSeatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(seat.getPlane()).isEqualTo(planeTest)
				);	
	}

	@Test
	void SeatMapper_fromDomainToDto_ReturnSeatDto() {
		
		SeatDto seatDto = seatMapper.fromDomainToDto(seatTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(seatDto).isNotNull(),
				()-> Assertions.assertThat(seatDto.seatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(seatDto.seatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(seatDto.planeId()).isEqualTo(planeTest.getPlaneId())
				);	
	}

	@Test
	void SeatMapper_fromListDomainToListDto_ReturnListSeatDto() {
		
		seatList.add(seatTest);
		List<SeatDto>  list = seatMapper.fromListDomainToListDto(seatList);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).seatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(list.get(0).seatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(list.get(0).planeId()).isEqualTo(planeTest.getPlaneId())
				);	
	}

	@Test
	void SeatMapper_fromListDomainToListDto_ReturnListSeatDtoEmpty() {		
		
		List<SeatDto>  list = seatMapper.fromListDomainToListDto(null);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);	
	}
	
}
