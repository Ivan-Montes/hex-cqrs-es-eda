package dev.ime.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import dev.ime.config.PlaneMapper;
import dev.ime.domain.model.Plane;
import dev.ime.domain.port.outbound.GenericNoSqlReadRepositoryPort;
import dev.ime.infrastructure.repository.read.PlaneNoSqlReadRepository;

@Repository
public class PlaneNoSqlReadRepositoryAdapter implements GenericNoSqlReadRepositoryPort<Plane>{

	private final PlaneNoSqlReadRepository planeNoSqlReadRepository;
	private final PlaneMapper planeMapper;	
	
	public PlaneNoSqlReadRepositoryAdapter(PlaneNoSqlReadRepository planeNoSqlReadRepository, PlaneMapper planeMapper) {
		super();
		this.planeNoSqlReadRepository = planeNoSqlReadRepository;
		this.planeMapper = planeMapper;
	}

	@Override
	public List<Plane> findAll(Integer page, Integer size) {
		
		return planeMapper.fromListMongoToListDomain( planeNoSqlReadRepository.findAll(PageRequest.of(page, size)).toList() );
		
	}

	@Override
	public Optional<Plane> findById(UUID id) {
		
		return planeNoSqlReadRepository.findFirstByPlaneId(id).map(planeMapper::fromMongoToDomain);
		
	}

}
