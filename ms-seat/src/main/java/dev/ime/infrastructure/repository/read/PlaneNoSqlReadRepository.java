package dev.ime.infrastructure.repository.read;

import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.ime.infrastructure.entity.PlaneMongoEntity;

@Qualifier("readMongoTemplate")
public interface PlaneNoSqlReadRepository extends MongoRepository<PlaneMongoEntity, ObjectId>{
	
	Optional<PlaneMongoEntity> findFirstByPlaneId(UUID id);
	
}
