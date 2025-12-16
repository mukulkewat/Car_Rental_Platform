package com.crp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crp.model.Car;

@Repository
public interface CarRepo extends JpaRepository<Car, Long> {

    // Find cars that are available and not booked in the given date range
    @Query("SELECT c FROM Car c WHERE c.available = true AND " +
           "c.id NOT IN (SELECT b.car.id FROM Booking b " +
           "WHERE (b.startDate <= :end AND b.endDate >= :start))")
    List<Car> findAvailableCars(@Param("start") LocalDate start,
                                @Param("end") LocalDate end);
}
