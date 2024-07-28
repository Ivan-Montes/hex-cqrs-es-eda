package dev.ime.application.service;


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

import dev.ime.application.dispatch.PlaneQueryDispatcher;
import dev.ime.domain.model.Plane;
import dev.ime.domain.query.Query;
import dev.ime.domain.query.QueryHandler;

@ExtendWith(MockitoExtension.class)
class PlaneQueryServiceTest {

	@Mock
	private PlaneQueryDispatcher planeQueryDispatcher;
	
	@InjectMocks
	private PlaneQueryService planeQueryService;

	@Mock
	private QueryHandler<Object> queryHandler;
	
	private List<Plane> planeList;
	private Plane planeTest;
	private final UUID planeId = UUID.randomUUID();
	private final String name = "Pursuit Special";
	private final Integer capacity = 2;
	
	@BeforeEach
	private void createObjects() {
		
		planeList  = new ArrayList<>();
		
		planeTest = new Plane(
				planeId,
				name,
				capacity
				);
	}
	
	@Test
	void PlaneQueryService_getAll_ReturnListPlane() {
		
		planeList.add(planeTest);
		Mockito.when(planeQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(planeList);
		
		List<Plane> list = planeQueryService.getAll(0,1);

		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(list).isNotNull(),
				()-> Assertions.assertThat(list).hasSize(1),
				()-> Assertions.assertThat(list.get(0).getPlaneId()).isEqualTo(planeId),
				()-> Assertions.assertThat(list.get(0).getName()).isEqualTo(name),
				()-> Assertions.assertThat(list.get(0).getCapacity()).isEqualTo(capacity)
				);	
	}

	@Test
	void PlaneQueryService_getById_ReturnOptionalPlane() {
		
		Mockito.when(planeQueryDispatcher.getQueryHandler(Mockito.any(Query.class))).thenReturn(queryHandler);
		Mockito.when(queryHandler.handle(Mockito.any(Query.class))).thenReturn(Optional.ofNullable(planeTest));
	
		Optional<Plane> optPlane = planeQueryService.getById(planeId);
		
		org.junit.jupiter.api.Assertions.assertAll(
				()-> Assertions.assertThat(optPlane).isNotNull(),
				()-> Assertions.assertThat(optPlane.get().getPlaneId()).isEqualTo(planeId),
				()-> Assertions.assertThat(optPlane.get().getName()).isEqualTo(name),
				()-> Assertions.assertThat(optPlane.get().getCapacity()).isEqualTo(capacity),
				()-> Assertions.assertThat(optPlane.get().toString()).hasToString(planeTest.toString()),
				()-> Assertions.assertThat(optPlane.get().hashCode()).hasSameHashCodeAs(planeTest.hashCode()),
				()-> Assertions.assertThat(optPlane).contains(planeTest)
				);
		
	}	

}
