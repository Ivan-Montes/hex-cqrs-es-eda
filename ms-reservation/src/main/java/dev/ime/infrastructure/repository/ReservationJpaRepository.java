package dev.ime.infrastructure.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.ime.infrastructure.entity.ReservationJpaEntity;
import jakarta.persistence.Tuple;

public interface ReservationJpaRepository extends JpaRepository<ReservationJpaEntity, UUID>{

	@Query( value="SELECT r.reservation_id, r.client_id, r.flight_id, s.seat_id "
			+ "FROM reservations r "
			+ "INNER JOIN seats s ON s.reservation_id = r.reservation_id "
			+ "WHERE r.flight_id = :flightId AND seat_id IN(:seatIdsSet) "
			+ "ORDER BY r.reservation_id", 
			nativeQuery=true )
	List<Tuple> findReservationJpaEntityByFlightIdAndSeatIdSetQueryNative(@Param("flightId")UUID flightId, @Param("seatIdsSet")Collection<UUID> seatIdsSet);
	
	@Query( value="SELECT COUNT(*) "
			+ "FROM reservations r "
			+ "INNER JOIN seats s ON s.reservation_id = r.reservation_id "
			+ "WHERE r.flight_id = :flightId AND seat_id IN(:seatIdsSet)", 
			nativeQuery=true )
	int countReservationJpaEntityByFlightIdAndSeatIdSet(@Param("flightId")UUID flightId, @Param("seatIdsSet")Collection<UUID> seatIdsSet);
	
	@Query( value="SELECT COUNT(*) "
			+ "FROM reservations r "
			+ "INNER JOIN seats s ON s.reservation_id = r.reservation_id "
			+ "WHERE r.flight_id = :flightId AND seat_id IN(:seatIdsSet) and client_id != :clientId", 
			nativeQuery=true )
	int countReservationJpaEntityByFlightIdSeatIdSetButDifferentClient(@Param("flightId")UUID flightId, @Param("seatIdsSet")Collection<UUID> seatIdsSet, @Param("clientId")UUID clientId);

	@Query( value="SELECT COUNT(*) "
			+ "FROM reservations r "
			+ "INNER JOIN seats s ON s.reservation_id = r.reservation_id "
			+ "WHERE r.reservation_id != :reservationId AND r.flight_id = :flightId and seat_id IN(:seatIdsSet) and client_id = :clientId", 
			nativeQuery=true )
	int countReservationJpaEntityByFlightIdSeatIdSetWithSameClientDifferentReservation(@Param("reservationId")UUID reservationId, @Param("flightId")UUID flightId, @Param("seatIdsSet")Collection<UUID> seatIdsSet, @Param("clientId")UUID clientId);
	
	
}
