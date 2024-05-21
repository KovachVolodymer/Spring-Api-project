package com.spring.jwt.mongodb.services.flight;

import com.spring.jwt.mongodb.models.flight.Flight;
import com.spring.jwt.mongodb.models.Reviews;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FlightService {

    ResponseEntity<List<Map<String, Object>>> flights();

    ResponseEntity<Flight> flightById(String id);

    ResponseEntity<Flight> addFlight(Flight flight);

    ResponseEntity<Flight> updateFlight(String id, Flight flight);

    ResponseEntity<Flight> patchFlight(String id, Flight flight);

    ResponseEntity<Object> deleteFlight(String id);

    ResponseEntity<List<String>> getUniqueAirlineName();

    ResponseEntity<Object> addReview(String id, Reviews review);

    ResponseEntity<Object> filter(String maxPrice, String minPrice, String airLine, String rating,
                                  String sort, String departureTime, String arrivalTime);

    ResponseEntity<List<String>> getUniqueCities();
}
