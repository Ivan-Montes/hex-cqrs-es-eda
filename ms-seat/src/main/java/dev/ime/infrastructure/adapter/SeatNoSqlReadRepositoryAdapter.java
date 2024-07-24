package dev.ime.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import dev.ime.config.SeatMapper;
import dev.ime.domain.model.Seat;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;
import dev.ime.domain.port.outbound.SeatSpecificNoSqlReadRepositoryPort;
import dev.ime.infrastructure.entity.SeatMongoEntity;
import dev.ime.infrastructure.repository.read.PlaneNoSqlReadRepository;
import dev.ime.infrastructure.repository.read.SeatNoSqlReadRepository;

@Repository
public class SeatNoSqlReadRepositoryAdapter implements GenericNoSqlReadRepositoryPort<Seat>, SeatSpecificNoSqlReadRepositoryPort<Seat>{

	private final SeatNoSqlReadRepository seatNoSqlReadRepository;
	private final SeatMapper seatMapper;
	private final PlaneNoSqlReadRepository planeNoSqlReadRepository;	

	public SeatNoSqlReadRepositoryAdapter(SeatNoSqlReadRepository seatNoSqlReadRepository, SeatMapper seatMapper,
			PlaneNoSqlReadRepository planeNoSqlReadRepository) {
		super();
		this.seatNoSqlReadRepository = seatNoSqlReadRepository;
		this.seatMapper = seatMapper;
		this.planeNoSqlReadRepository = planeNoSqlReadRepository;
	}

	@Override
	public List<Seat> findAll(Integer page, Integer size) {
		
		List<SeatMongoEntity> seatMongoList = seatNoSqlReadRepository.findAll(PageRequest.of(page, size)).toList();
		
		return buildSeatMongoList(seatMongoList);
		
	}

	@Override
	public Optional<Seat> findById(UUID id) {

		return seatNoSqlReadRepository.findFirstBySeatId(id)
		        .flatMap(mongoSeat -> planeNoSqlReadRepository.findFirstByPlaneId(mongoSeat.getPlaneId())
		            .map(mongoPlane -> seatMapper.fromMongoToDomain(mongoSeat, mongoPlane)));
		
	}

	@Override
	public List<Seat> findByPlaneId(UUID id) {
		
		List<SeatMongoEntity> seatMongoList = seatNoSqlReadRepository.findByPlaneId(id);
		
		return buildSeatMongoList(seatMongoList);		
	
	}

	private List<Seat> buildSeatMongoList(List<SeatMongoEntity> seatMongoList) {
		
		return seatMongoList.stream()
				.map(seatMongoEntity -> planeNoSqlReadRepository.findFirstByPlaneId(seatMongoEntity.getPlaneId())
				.map(planeMongoEntity -> seatMapper.fromMongoToDomain(seatMongoEntity, planeMongoEntity)))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.toList();
		
	}

}
