package com.spring.jwt.mongodb.repository;

import com.spring.jwt.mongodb.models.Flight;
import lombok.Data;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface FlightsRepository extends MongoRepository<Flight, String> {


    @Query("""
             { 'price' : { $gte : ?0, $lte : ?1 },
             'airlineName' : { $regex : ?2, $options: 'i' },
             'rating' : { $gte : ?3 },
             'departureTime' : { $gte : ?4 } }""")
    List<Flight> filter(
            Integer minPrice, Integer maxPrice, String airlineName, Double rating, LocalDateTime departureTime
    );


}
