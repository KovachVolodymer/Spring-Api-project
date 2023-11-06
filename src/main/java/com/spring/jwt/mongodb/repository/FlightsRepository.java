package com.spring.jwt.mongodb.repository;

import com.spring.jwt.mongodb.models.Flights;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FlightsRepository  extends MongoRepository<Flights, String> {
    Optional<Flights> findByFlightId(Integer flightId);

    void deleteByFlightId(Integer id);
}
