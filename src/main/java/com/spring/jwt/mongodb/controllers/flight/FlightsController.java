package com.spring.jwt.mongodb.controllers.flight;

import com.spring.jwt.mongodb.models.flight.Flight;
import com.spring.jwt.mongodb.models.Reviews;
import com.spring.jwt.mongodb.services.flight.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/flights")
public class FlightsController {
    @Autowired
    FlightService flightService;

    @GetMapping("")
    public ResponseEntity<List<Map<String, Object>>> flights() {
       return flightService.flights();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> flightById(@PathVariable String id) {
        return flightService.flightById(id);
    }

    @PostMapping("")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Flight> addFlight(@RequestBody Flight flight) {
        return flightService.addFlight(flight);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Flight> updateFlight(@PathVariable String id, @RequestBody Flight flight) {
        return flightService.updateFlight(id, flight);
    }

    @PatchMapping("/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Flight> patchFlight(@PathVariable String id, @RequestBody Flight flight) {
     return flightService.patchFlight(id, flight);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteFlight(@PathVariable String id) {
        return flightService.deleteFlight(id);
    }

    @GetMapping("/airlines")
    public ResponseEntity<List<String>> getUniqueAirlineName() {
        return flightService.getUniqueAirlineName();
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<Object> addReview(@PathVariable String id, @RequestBody Reviews review) {
        return flightService.addReview(id, review);
    }

}
