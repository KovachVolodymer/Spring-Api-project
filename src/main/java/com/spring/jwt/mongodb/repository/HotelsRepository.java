package com.spring.jwt.mongodb.repository;

import com.spring.jwt.mongodb.models.Hotels;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HotelsRepository extends MongoRepository<Hotels, String> {

    @Query("{'price': {$gte: ?0, $lte: ?1}}")
    List<Hotels> findByPriceBetween(String min, String max);
}
