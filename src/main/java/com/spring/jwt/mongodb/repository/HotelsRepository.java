package com.spring.jwt.mongodb.repository;

import com.spring.jwt.mongodb.models.hotel.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface HotelsRepository extends MongoRepository<Hotel, String> {



    @Query("""
            {'price' : { $gte : ?0, $lte : ?1 },
            'rating' : { $gte : ?2 },
            'advantages' : { $all : ?3} }""")

    List<Hotel> filter(Integer minPrice, Integer maxPrice, Double rating, List<String> advantages);

    @Query("""
            { 'price' : { $gte : ?0, $lte : ?1 },
            'rating' : { $gte : ?2 } }""")
    List<Hotel> filter(Integer minPrice, Integer maxPrice, Double rating);
}
