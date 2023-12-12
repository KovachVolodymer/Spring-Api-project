package com.spring.jwt.mongodb.repository;

import com.spring.jwt.mongodb.models.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FlightsRepository extends MongoRepository<Flight, String> {

    List<Flight> findByAirlineNameContainsIgnoreCase(String AirLine);

    List<Flight> findByPriceIsBetween(Integer minPrice, Integer maxPrice);

    @Query("{ 'price' : { $gte : ?0, $lte : ?1 }, "
            +"'airlineName' : { $regex : ?2, $options: 'i' }, "
            +"'rating' : { $gte : ?3 } }")
    List<Flight> findByPriceBetweenAndAirlineNameContainingIgnoreCaseAndRatingGreaterThanEqual(
            Integer minPrice, Integer maxPrice, String airlineName, Double rating
    );
}
