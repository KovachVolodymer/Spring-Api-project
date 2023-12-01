package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Flights;
import com.spring.jwt.mongodb.models.Reviews;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/flights")
public class FlightsController {

    @Autowired
    private FlightsRepository flightsRepository;

    @GetMapping("")
    public ResponseEntity<List<Map<String, Object>>> flights() {
        List<Flights> flightsList = flightsRepository.findAll();
        List<Map<String, Object>> flightMaps = flightsList.stream()
                .map(flight -> {
                    Map<String, Object> flightMap = new HashMap<>();
                    flightMap.put("flightId", flight.getFlightId());
                    flightMap.put("airlineLogo", flight.getAirlineLogo());
                    flightMap.put("airlineName", flight.getAirlineName());
                    flightMap.put("ALT", flight.getAlt());
                    flightMap.put("geolocation", flight.getGeolocation());
                    flightMap.put("price", flight.getPrice());
                    flightMap.put("duration", flight.getDuration());
                    flightMap.put("abbreviation", flight.getAbbreviation());
                    flightMap.put("rating", flight.getRating());

                    return flightMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(flightMaps);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Flights> flightById(@PathVariable Integer id) {
        Optional<Flights> flightData = flightsRepository.findByFlightId(id);
        return flightData.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @PostMapping("")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Flights> addFlight(@RequestBody Flights flight) {
            Optional<Flights> existingFlight = flightsRepository.findByFlightId(flight.getFlightId());
            if (existingFlight.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            } else {
                Flights newFlight = flightsRepository.save(flight);
                return ResponseEntity.ok(newFlight);
            }
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Flights> updateFlight(@PathVariable Integer id, @RequestBody Flights flight) {
        Optional<Flights> flightData = flightsRepository.findByFlightId(id);
        flightData.ifPresent(flg -> {
            Optional.ofNullable(flight.getAirlineLogo()).ifPresent(flg::setAirlineLogo);
            Optional.ofNullable(flight.getAirlineName()).ifPresent(flg::setAirlineName);
            Optional.ofNullable(flight.getAlt()).ifPresent(flg::setAlt);
            Optional.ofNullable(flight.getGeolocation()).ifPresent(flg::setGeolocation);
            Optional.ofNullable(flight.getPrice()).ifPresent(flg::setPrice);
            Optional.ofNullable(flight.getDuration()).ifPresent(flg::setDuration);
            Optional.ofNullable(flight.getAbbreviation()).ifPresent(flg::setAbbreviation);
            Optional.ofNullable(flight.getRating()).ifPresent(flg::setRating);
            Optional.ofNullable(flight.getAdvantages()).ifPresent(flg::setAdvantages);
            flightsRepository.save(flg);

        });
        return flightData.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteFlight(@PathVariable Integer id) {
        Optional<Flights> flightData = flightsRepository.findByFlightId(id);
        if (flightData.isPresent()) {
            try {
                flightsRepository.deleteByFlightId(id);
                return ResponseEntity.ok("Політ було успішно видалено.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Помилка при видаленні політу.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Політ з вказаним ID не знайдено.");
        }
    }

    @GetMapping("/favorites")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Flights>> favoriteFlights(@RequestParam List<String> favoriteFlights) {
        List<Flights> flightsList = flightsRepository.findAllById(favoriteFlights);
        return ResponseEntity.ok(flightsList);
    }

    @PostMapping("/reviews")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Flights> addReview( @RequestBody Reviews review) {
        Optional<Flights> flightData = flightsRepository.findByFlightId(review.getId());
        flightData.ifPresent(flight -> {
            if (flight.getReviewsList() == null) {
                flight.setReviewsList(new ArrayList<>());
            }
            flight.getReviewsList().add(review);
            flightsRepository.save(flight);
        });
        return flightData.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }



}
