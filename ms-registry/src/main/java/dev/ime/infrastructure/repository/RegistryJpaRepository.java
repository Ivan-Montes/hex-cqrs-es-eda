package dev.ime.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.ime.infrastructure.entity.RegistryJpaEntity;

public interface RegistryJpaRepository extends JpaRepository<RegistryJpaEntity, UUID>{

}
