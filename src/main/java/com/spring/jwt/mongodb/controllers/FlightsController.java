package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Flights;
import com.spring.jwt.mongodb.models.Hotels;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/flights")
public class FlightsController {

    @Autowired
    private FlightsRepository flightsRepository;

    @GetMapping("/allFlights")
    public ResponseEntity<List<Flights>> flights() {
        List<Flights> flightsList = flightsRepository.findAll();
        return ResponseEntity.ok(flightsList);
    }

    @GetMapping("/flight/{id}")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Flights> flightById(@PathVariable Integer id) {
        Optional<Flights> flightData = flightsRepository.findByFlightId(id);
        return flightData.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @PostMapping("/addFlight")
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

    @PutMapping("/updateFlight/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Flights> updateFlight(@PathVariable Integer id, @RequestBody Flights flight) {
        Optional<Flights> flightData = flightsRepository.findByFlightId(id);
        if (flightData.isPresent()) {
            Flights _flight = flightData.get();

            if (flight.getFlightName() != null) {
                _flight.setFlightName(flight.getFlightName());
            }
            if (flight.getFlightFrom() != null) {
                _flight.setFlightFrom(flight.getFlightFrom());
            }
            if (flight.getFlightTo() != null) {
                _flight.setFlightTo(flight.getFlightTo());
            }
            if (flight.getFlightDate() != null) {
                _flight.setFlightDate(flight.getFlightDate());
            }
            if (flight.getFlightTime() != null) {
                _flight.setFlightTime(flight.getFlightTime());
            }
            if (flight.getFlightPrice() != null) {
                _flight.setFlightPrice(flight.getFlightPrice());
            }
            if (flight.getFlightDuration() != null) {
                _flight.setFlightDuration(flight.getFlightDuration());
            }
            if (flight.getFlightReviews() != null) {
                _flight.setFlightReviews(flight.getFlightReviews());
            }
            if (flight.getFlightClass() != null) {
                _flight.setFlightClass(flight.getFlightClass());
            }
            if (flight.getFlightSeats() != null) {
                _flight.setFlightSeats(flight.getFlightSeats());
            }
            if (flight.getFlightAirlineLogo() != null) {
                _flight.setFlightAirlineLogo(flight.getFlightAirlineLogo());
            }
            if (flight.getFlightAdvantages() != null) {
                _flight.setFlightAdvantages(flight.getFlightAdvantages());
            }
            if (flight.getFlightPhotos() != null) {
                _flight.setFlightPhotos(flight.getFlightPhotos());
            }

            return ResponseEntity.ok(flightsRepository.save(_flight));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteFlight/{id}")
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

    @GetMapping("/favoriteFlights")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Flights>> favoriteFlights(@RequestParam List<String> favoriteFlights) {
        List<Flights> flightsList = flightsRepository.findAllById(favoriteFlights);
        return ResponseEntity.ok(flightsList);
    }



}
