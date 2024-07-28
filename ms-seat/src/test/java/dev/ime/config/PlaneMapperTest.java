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

import dev.ime.application.dto.PlaneDto;
import dev.ime.domain.model.Plane;
import dev.ime.infrastructure.entity.PlaneMongoEntity;

@ExtendWith(MockitoExtension.class)
class PlaneMapperTest {

	@InjectMocks
	private PlaneMapper planeMapper;
	
	private List<Plane> planeList;
	private List<PlaneMongoEntity> planeMongoEntityList;
	private Plane planeTest;
	private PlaneMongoEntity planeMongoEntityTest;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;	

	@BeforeEach
	private void createObjects() {
		
		planeList = new ArrayList<>();
		planeMongoEntityList = new ArrayList<>();
		
		planeTest = new Plane();
		planeTest.setPlaneId(planeId);
		planeTest.setName(name);
		planeTest.setCapacity(capacity);
		
		planeMongoEntityTest = new PlaneMongoEntity();
		planeMongoEntityTest.setPlaneId(planeId);
		planeMongoEntityTest.setName(name);
		planeMongoEntityTest.setCapacity(capacity);
		
	}
	
	@Test
	void PlaneMapper_fromMongoToDomain_ReturnPlane() {
		
		Plane plane = planeMapper.fromMongoToDomain(planeMongoEntityTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(plane).isNotNull(),
				()-> Assertions.assertThat(plane.getPlaneId()).isEqualTo(planeId),
				()-> Assertions.assertThat(plane.getName()).isEqualTo(name),
				()-> Assertions.assertThat(plane.getCapacity()).isEqualTo(capacity)
				);		
	}

	@Test
	void PlaneMapper_fromListMongoToListDomain_ReturnListPlane() {
		
		planeMongoEntityList.add(planeMongoEntityTest);
		List<Plane> list = planeMapper.fromListMongoToListDomain(planeMongoEntityList);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getPlaneId()).isEqualTo(planeId),
				()-> Assertions.assertThat(list.get(0).getName()).isEqualTo(name),
				()-> Assertions.assertThat(list.get(0).getCapacity()).isEqualTo(capacity)
				);
	}

	@Test
	void PlaneMapper_fromListMongoToListDomain_ReturnListPlaneNull() {
		
		List<Plane> list = planeMapper.fromListMongoToListDomain(null);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);	
	}

	@Test
	void PlaneMapper_fromDomainToDto_ReturnPlaneDto() {
		
		PlaneDto planeDto = planeMapper.fromDomainToDto(planeTest);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(planeDto).isNotNull(),
				()-> Assertions.assertThat(planeDto.planeId()).isEqualTo(planeId),
				()-> Assertions.assertThat(planeDto.name()).isEqualTo(name),
				()-> Assertions.assertThat(planeDto.capacity()).isEqualTo(capacity.intValue())
				);
	}

	@Test
	void PlaneMapper_fromListDomainToListDto_ReturnListPlaneDto() {
		
		planeList.add(planeTest);
		
		List<PlaneDto> list = planeMapper.fromListDomainToListDto(planeList);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).planeId()).isEqualTo(planeId),
				()-> Assertions.assertThat(list.get(0).name()).isEqualTo(name),
				()-> Assertions.assertThat(list.get(0).capacity()).isEqualTo(capacity)
				);
	}

	@Test
	void PlaneMapper_fromListDomainToListDto_ReturnListPlaneDtoNull() {
		
		List<PlaneDto> list = planeMapper.fromListDomainToListDto(null);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).isEmpty()
				);	
	}
	
}
