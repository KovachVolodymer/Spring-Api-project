package com.spring.jwt.mongodb.repository;

import com.spring.jwt.mongodb.models.Hotels;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HotelsRepository extends MongoRepository<Hotels, String> {


}
