package dev.ime.infrastructure.repository.read;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.repository.MongoRepository;

import dev.ime.infrastructure.entity.SeatMongoEntity;

@Qualifier("readMongoTemplate")
public interface SeatNoSqlReadRepository extends MongoRepository<SeatMongoEntity, ObjectId> {
	
	Optional<SeatMongoEntity> findFirstBySeatId(UUID id);
	List<SeatMongoEntity> findByPlaneId(UUID planeId);
	
}
