package com.spring.jwt.mongodb.repository;

import com.spring.jwt.mongodb.models.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface HotelsRepository extends MongoRepository<Hotel, String> {


    List<Hotel> findByPriceGreaterThanEqual(Integer minPrice);

    @Query("""
            { 'price' : { $gte : ?0, $lte : ?1 },
            'starRating' : { $gte : ?2 },
            'advantages' : { $all : ?3} }""")

    List<Hotel> filter(Integer minPrice, Integer maxPrice, Double Rating, List<String> advantages);

}
