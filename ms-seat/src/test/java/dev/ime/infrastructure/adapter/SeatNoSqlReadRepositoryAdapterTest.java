package dev.ime.infrastructure.adapter;


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

import dev.ime.config.SeatMapper;
import dev.ime.domain.model.Plane;
import dev.ime.domain.model.Seat;
import dev.ime.infrastructure.entity.PlaneMongoEntity;
import dev.ime.infrastructure.entity.SeatMongoEntity;
import dev.ime.infrastructure.repository.read.PlaneNoSqlReadRepository;
import dev.ime.infrastructure.repository.read.SeatNoSqlReadRepository;

@ExtendWith(MockitoExtension.class)
class SeatNoSqlReadRepositoryAdapterTest {

	@Mock
	private SeatNoSqlReadRepository seatNoSqlReadRepository;
	
	@Mock
	private SeatMapper seatMapper;
	
	@Mock
	private PlaneNoSqlReadRepository planeNoSqlReadRepository;	

	@InjectMocks
	private SeatNoSqlReadRepositoryAdapter seatNoSqlReadRepositoryAdapter;

	private List<SeatMongoEntity> seatMongoEntityList;
	private Seat seatTest;
	private SeatMongoEntity seatMongoEntityTest;
	private Plane planeTest;
	private PlaneMongoEntity planeMongoEntityTest;
	private final ObjectId objectId = ObjectId.get();
	private final UUID seatId = UUID.randomUUID();
	private final String seatNumber = "PS1973";
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;

	@BeforeEach
	private void createObjects() {
		
		seatMongoEntityList = new ArrayList<>();
		
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		planeTest.setName(name);
		planeTest.setCapacity(capacity);
		
		seatTest = new Seat(
				seatId,
				seatNumber,
				planeTest);
		
		seatMongoEntityTest = new SeatMongoEntity();
		seatMongoEntityTest.setMongoId(objectId);
		seatMongoEntityTest.setSeatId(seatId);
		seatMongoEntityTest.setSeatNumber(seatNumber);
		seatMongoEntityTest.setPlaneId(planeId);

		planeMongoEntityTest = new PlaneMongoEntity();
		planeMongoEntityTest.setMongoId(objectId);
		planeMongoEntityTest.setPlaneId(planeId);
		planeMongoEntityTest.setName(name);
		planeMongoEntityTest.setCapacity(capacity);
	}	
	
	@Test
	void SeatNoSqlReadRepositoryAdapter_findAll_ReturnListSeat() {
		
		seatMongoEntityList.add(seatMongoEntityTest);	
		@SuppressWarnings("unchecked")
		Page<SeatMongoEntity> pageMock = Mockito.mock(Page.class);
		Mockito.when(seatNoSqlReadRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(pageMock);
		Mockito.when(pageMock.toList()).thenReturn(seatMongoEntityList);
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(planeMongoEntityTest));
		Mockito.when(seatMapper.fromMongoToDomain(Mockito.any(SeatMongoEntity.class), Mockito.any(PlaneMongoEntity.class))).thenReturn(seatTest);
		
		List<Seat> list = seatNoSqlReadRepositoryAdapter.findAll(0,1);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(list.get(0).getSeatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(list.get(0).getPlane()).isEqualTo(planeTest)
				);
	}

	@Test
	void SeatNoSqlReadRepositoryAdapter_findById_ReturnOptionalSeat() {
		
		Mockito.when(seatNoSqlReadRepository.findFirstBySeatId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(seatMongoEntityTest));
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(planeMongoEntityTest));
		Mockito.when(seatMapper.fromMongoToDomain(Mockito.any(SeatMongoEntity.class), Mockito.any(PlaneMongoEntity.class))).thenReturn(seatTest);

		Optional<Seat> optSeat = seatNoSqlReadRepositoryAdapter.findById(seatId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optSeat).isNotNull(),
				()-> Assertions.assertThat(optSeat.get().getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(optSeat.get().getSeatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(optSeat.get().getPlane()).isEqualTo(planeTest)
				);	
	}

	@Test
	void SeatNoSqlReadRepositoryAdapter_findByPlaneId_ReturnListSeat() {
		
		seatMongoEntityList.add(seatMongoEntityTest);
		Mockito.when(seatNoSqlReadRepository.findByPlaneId(Mockito.any(UUID.class))).thenReturn(seatMongoEntityList);
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(planeMongoEntityTest));
		Mockito.when(seatMapper.fromMongoToDomain(Mockito.any(SeatMongoEntity.class), Mockito.any(PlaneMongoEntity.class))).thenReturn(seatTest);
		
		List<Seat> list = seatNoSqlReadRepositoryAdapter.findByPlaneId(planeId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getSeatId()).isEqualTo(seatId),
				()-> Assertions.assertThat(list.get(0).getSeatNumber()).isEqualTo(seatNumber),
				()-> Assertions.assertThat(list.get(0).getPlane()).isEqualTo(planeTest)
				);		
	}	
	
}
