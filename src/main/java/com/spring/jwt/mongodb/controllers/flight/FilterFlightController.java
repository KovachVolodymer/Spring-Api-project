package com.spring.jwt.mongodb.controllers.flight;

import com.spring.jwt.mongodb.models.Flight;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import com.spring.jwt.mongodb.services.flight.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/flights")
public class FilterFlightController {
    @Autowired
    FlightService flightService;


    @GetMapping("/filter")
    public ResponseEntity<Object> filter(
            @RequestParam(name = "maxPrice", defaultValue = "500") String maxPrice,
            @RequestParam(name = "minPrice", defaultValue = "0") String minPrice,
            @RequestParam(name = "airLine", defaultValue = "") String airLine,
            @RequestParam(name = "rating", defaultValue = "0.0") String rating,
            @RequestParam(name = "sort", defaultValue = "") String sort,
            @RequestParam(name = "departureTime", defaultValue = "2024-04-20T00:00:00") String departureTime) {
        return flightService.filter(maxPrice, minPrice, airLine, rating, sort, departureTime);
    }


}
