package dev.ime.infrastructure.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import dev.ime.config.RegistryMapper;
import dev.ime.domain.model.Registry;
import dev.ime.domain.port.outbound.GenericReadRepositoryPort;
import dev.ime.infrastructure.repository.RegistryJpaRepository;

@Repository
public class RegistryReadRepositoryAdapter implements GenericReadRepositoryPort<Registry>{

	private final RegistryJpaRepository registryJpaRepository;
	private final RegistryMapper registryMapper;
	
	public RegistryReadRepositoryAdapter(RegistryJpaRepository registryJpaRepository, RegistryMapper registryMapper) {
		super();
		this.registryJpaRepository = registryJpaRepository;
		this.registryMapper = registryMapper;
	}

	@Override
	public List<Registry> findAll(Integer page, Integer size) {
		
		return registryMapper.fromListJpaToListDomain(registryJpaRepository.findAll(PageRequest.of(page, size)).toList());
	
	}

	@Override
	public Optional<Registry> findById(UUID id) {
		
		return registryJpaRepository.findById(id).map(registryMapper::fromJpaToDomain);
		
	}

}
