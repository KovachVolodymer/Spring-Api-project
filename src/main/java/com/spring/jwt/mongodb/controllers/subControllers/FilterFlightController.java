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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FilterFlightController {

    @Autowired
    FlightsRepository flightsRepository;


    @GetMapping("/filter")
    public ResponseEntity<Object> filter(
            @RequestParam(name = "maxPrice", defaultValue = "500") Integer maxPrice,
            @RequestParam(name = "minPrice", defaultValue = "0") Integer minPrice,
            @RequestParam(name = "airLine", defaultValue = "") String airLine,
            @RequestParam(name = "rating", defaultValue = "0.0") Double rating,
            @RequestParam(name = "sort", defaultValue = "") String sort) {


        List<Flight> flights = flightsRepository.filter(minPrice, maxPrice, airLine, rating);

        switch (sort)
        {
            case "maxPrice":
                flights.sort((f1, f2) -> f2.getPrice().compareTo(f1.getPrice()));
                break;
            case "minPrice":
                flights.sort(Comparator.comparing(Flight::getPrice));
                break;
            case "rating":
                flights.sort((f1, f2) -> f2.getRating().compareTo(f1.getRating()));
                break;
            case "airLine":
                flights.sort((f1, f2) -> f2.getAirlineName().compareTo(f1.getAirlineName()));
                break;
            default:
                break;
        }

        return flights.isEmpty()
                ? ResponseEntity.badRequest().body("No flights found")
                : ResponseEntity.ok(flights);
    }

}
