package com.spring.jwt.mongodb.repository;

import com.spring.jwt.mongodb.models.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FlightsRepository extends MongoRepository<Flight, String> {

}
