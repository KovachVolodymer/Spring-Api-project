package com.spring.jwt.mongodb.controllers.subControllers;

import com.spring.jwt.mongodb.models.Flight;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/flights")
public class FilterFlightController {

    @Autowired
    FlightsRepository flightsRepository;


    @GetMapping("/filter")
    public ResponseEntity<Object> filter(
            @RequestParam(name = "maxPrice", defaultValue = "500") String maxPrice,
            @RequestParam(name = "minPrice", defaultValue = "0") String minPrice,
            @RequestParam(name = "airLine", defaultValue = "") String airLine,
            @RequestParam(name = "rating", defaultValue = "0.0") String rating,
            @RequestParam(name = "sort", defaultValue = "") String sort,
            @RequestParam(name = "departureTime", defaultValue = "2024-04-20T00:00:00") String departureTime){

        int minPriceInt = Integer.parseInt(minPrice);
        int maxPriceInt = Integer.parseInt(maxPrice);
        double ratingDouble = Double.parseDouble(rating);


        List<Flight> flights = flightsRepository.filter(minPriceInt, maxPriceInt, airLine, ratingDouble, LocalDateTime.parse(departureTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        switch (sort)
        {
            case "Cheapest":
                flights.sort(Comparator.comparing(Flight::getPrice));
                break;
            case "Best":
                flights.sort((f1, f2) -> {
                    int result = f2.getRating().compareTo(f1.getRating());
                    if (result == 0) {
                        result = f1.getPrice().compareTo(f2.getPrice());
                    }
                    return result;
                });
                break;
            case "Quickest":
                flights.sort((f1, f2) -> f1.getDuration().compareTo(f2.getDuration()));
                break;
            default:
                break;
        }

        return flights.isEmpty()
                ? ResponseEntity.badRequest().body(new MessageResponse("No flights found"))
                : ResponseEntity.ok(flights);
    }


}
