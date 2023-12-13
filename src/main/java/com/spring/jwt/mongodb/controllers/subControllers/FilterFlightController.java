package com.spring.jwt.mongodb.controllers.subControllers;

import com.spring.jwt.mongodb.models.Flight;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FilterFlightController {

    @Autowired
    FlightsRepository flightsRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @GetMapping("/filter")
    public ResponseEntity<Object> filterByPrice(
            @RequestParam(name = "maxPrice", defaultValue = "500") Integer maxPrice,
            @RequestParam(name = "minPrice", defaultValue = "0") Integer minPrice,
            @RequestParam(name = "airLine", defaultValue = "") String airLine,
            @RequestParam(name = "rating", defaultValue = "0.0") Double rating) {

        List<Flight> flights =
                flightsRepository
                        .filter
                                (minPrice, maxPrice, airLine, rating);

        return flights.isEmpty()
                ? ResponseEntity.badRequest().body("No flights found")
                : ResponseEntity.ok(flights);
    }




}
