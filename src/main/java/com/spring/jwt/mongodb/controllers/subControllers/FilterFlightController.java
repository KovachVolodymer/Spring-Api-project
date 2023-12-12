package com.spring.jwt.mongodb.controllers.subControllers;

import com.spring.jwt.mongodb.models.Flight;
import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/filter")
    public ResponseEntity<Object> filterByPrice(@RequestParam(name = "maxPrice" ,defaultValue = "500") Integer maxPrice,
                                                @RequestParam (name = "minPrice", defaultValue = "0") Integer minPrice,
                                                @RequestParam (name = "AirLine", defaultValue = " ") String AirLine,
                                                @RequestParam (name = "Rating", defaultValue = "0") Double Rating)
    {

       List<Flight> flight = flightsRepository.findByPriceBetweenAndAirlineNameContainingIgnoreCaseAndRatingGreaterThanEqual(minPrice, maxPrice,AirLine,Rating );

        return flight.isEmpty()
                ? ResponseEntity.badRequest().body("No flight found")
                : ResponseEntity.ok(flight);
    }

}
