package com.spring.jwt.mongodb.repository;

import com.spring.jwt.mongodb.models.Hotel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface HotelsRepository extends MongoRepository<Hotel, String> {

    List<Hotel> findByPriceLessThanEqual(double maxPrice);

    List<Hotel> findByPriceGreaterThanEqual(double minPrice);
}
