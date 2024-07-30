package dev.ime.infrastructure.adapter;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import dev.ime.config.PlaneMapper;
import dev.ime.domain.model.Plane;
import dev.ime.infrastructure.entity.PlaneMongoEntity;
import dev.ime.infrastructure.repository.read.PlaneNoSqlReadRepository;

@ExtendWith(MockitoExtension.class)
class PlaneNoSqlReadRepositoryAdapterTest {

	@Mock
	private PlaneNoSqlReadRepository planeNoSqlReadRepository;

	@Mock
	private PlaneMapper planeMapper;	

	@InjectMocks
	private PlaneNoSqlReadRepositoryAdapter planeNoSqlReadRepositoryAdapter;	

	private List<Plane> planeList;
	private List<PlaneMongoEntity> planeMongoEntityList;
	private Plane planeTest;
	private PlaneMongoEntity planeMongoEntity;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	
	@BeforeEach
	private void createObjects() {
		
		planeList = new ArrayList<>();
		planeMongoEntityList  = new ArrayList<>();
		
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		planeTest.setName(name);
		planeTest.setCapacity(capacity);
		
		planeMongoEntity = new PlaneMongoEntity();
		planeMongoEntity.setPlaneId(planeId);
		planeMongoEntity.setName(name);
		planeMongoEntity.setCapacity(capacity);
		
	}
	
	
	@Test
	void PlaneNoSqlReadRepositoryAdapter_findAll_ReturnListPlane() {
		
		planeMongoEntityList.add(planeMongoEntity);
		planeList.add(planeTest);		
		@SuppressWarnings("unchecked")
		Page<PlaneMongoEntity> pageMock = Mockito.mock(Page.class);
		Mockito.when(planeNoSqlReadRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(pageMock);
		Mockito.when(pageMock.toList()).thenReturn(planeMongoEntityList);
		Mockito.when(planeMapper.fromListMongoToListDomain(Mockito.anyList())).thenReturn(planeList);
		
		List<Plane> list = planeNoSqlReadRepositoryAdapter.findAll(0,1);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getPlaneId()).isEqualTo(planeId),
				()-> Assertions.assertThat(list.get(0).getName()).isEqualTo(name),
				()-> Assertions.assertThat(list.get(0).getCapacity()).isEqualTo(capacity)
				);			
	}

	@Test
	void PlaneNoSqlReadRepositoryAdapter_findById_ReturnOptionalPlane() {
		
		Mockito.when(planeNoSqlReadRepository.findFirstByPlaneId(Mockito.any(UUID.class))).thenReturn(Optional.ofNullable(planeMongoEntity));
		Mockito.when(planeMapper.fromMongoToDomain(Mockito.any(PlaneMongoEntity.class))).thenReturn(planeTest);
		
		Optional<Plane> optPlane = planeNoSqlReadRepositoryAdapter.findById(planeId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optPlane).isNotNull(),
				()-> Assertions.assertThat(optPlane.get().getPlaneId()).isEqualTo(planeId),
				()-> Assertions.assertThat(optPlane.get().getName()).isEqualTo(name),
				()-> Assertions.assertThat(optPlane.get().getCapacity()).isEqualTo(capacity)
				);
	}
	
}
