package com.spring.jwt.mongodb.services.flight;

import com.spring.jwt.mongodb.models.flight.Flight;
import com.spring.jwt.mongodb.models.Reviews;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@Service
public class FlightServiceImpl implements FlightService{

    @Autowired
    private FlightsRepository flightsRepository;

    @Override
    public ResponseEntity<List<Map<String, Object>>> flights() {
        List<Flight> flightsList = flightsRepository.findAll();
        List<Map<String, Object>> flightMaps = flightsList.stream()
                .map(flight -> {
                    Map<String, Object> flightMap = new HashMap<>();
                    flightMap.put("id", flight.getId());
                    flightMap.put("alt", flight.getAlt());
                    flightMap.put("photo", flight.getPhoto());
                    flightMap.put("airlineName", flight.getAirlineName());
                    flightMap.put("rating", flight.getRating());
                    flightMap.put("price", flight.getPrice());
                    flightMap.put("location", flight.getGeolocation());
                    flightMap.put("duration", flight.getDuration());
                    flightMap.put("abbreviation", flight.getAbbreviation());
                    flightMap.put("slug", flight.getSlug());
                    flightMap.put("fromArrive", flight.getFromArrive());
                    flightMap.put("toArrive", flight.getToArrive());
                    flightMap.put("airlineLogo", flight.getAirlineLogo());
                    flightMap.put("departureTime", flight.getDepartureTime());
                    flightMap.put("arrivalTime", flight.getArrivalTime());

                    return flightMap;
                })
                .collect(Collectors.toList());

        return ok(flightMaps);
    }

    @Override
    public ResponseEntity<Flight> flightById(String id) {
        Optional<Flight> flightData = flightsRepository.findById(id);
        return flightData.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @Override
    public ResponseEntity<Flight> addFlight(Flight flight) {
        if (flight.getId() != null && flightsRepository.existsById(flight.getId())) {
            // If a flight with the specified id already exists, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            // If no id is specified or there is no existing flight with the given id, save the new flight
            String slug = flight.getAirlineName().toLowerCase()
                    .replaceAll(" ", "_")
                    .replaceAll("[^a-z0-9_-]", "");
            flight.setSlug(slug);
            Flight savedFlight = flightsRepository.save(flight);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFlight);
        }
    }

    @Override
    public ResponseEntity<Flight> updateFlight(String id, Flight flight) {
        Optional<Flight> flightData = flightsRepository.findById(id);
        if (flightData.isPresent()) {
            Flight saveFlight = flightData.get();
            saveFlight.setAirlineName(flight.getAirlineName());
            saveFlight.setPrice(flight.getPrice());
            saveFlight.setRating(flight.getRating());
            saveFlight.setPhoto(flight.getPhoto());
            saveFlight.setGeolocation(flight.getGeolocation());
            saveFlight.setDuration(flight.getDuration());
            saveFlight.setAbbreviation(flight.getAbbreviation());
            saveFlight.setAlt(flight.getAlt());
            saveFlight.setPartnerName(flight.getPartnerName());
            saveFlight.setFromArrive(flight.getFromArrive());
            saveFlight.setToArrive(flight.getToArrive());
            saveFlight.setAirlineLogo(flight.getAirlineLogo());
            saveFlight.setDepartureTime(flight.getDepartureTime());
            saveFlight.setArrivalTime(flight.getArrivalTime());
            saveFlight.setPartnerLogo(flight.getPartnerLogo());
            saveFlight.setAdvantages(flight.getAdvantages());
            saveFlight.setReviews(flight.getReviews());
            saveFlight.setSlug(flight.getSlug());
            saveFlight.setPlaneName(flight.getPlaneName());
            saveFlight.setAmount(flight.getAmount());
            return new ResponseEntity<>(flightsRepository.save(saveFlight), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Flight> patchFlight(String id, Flight flight) {
        Optional<Flight> flightData = flightsRepository.findById(id);
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
            Optional.ofNullable(flight.getPhoto()).ifPresent(flg::setPhoto);
            Optional.ofNullable(flight.getPartnerName()).ifPresent(flg::setPartnerName);
            Optional.ofNullable(flight.getFromArrive()).ifPresent(flg::setFromArrive);
            Optional.ofNullable(flight.getToArrive()).ifPresent(flg::setToArrive);
            Optional.ofNullable(flight.getArrivalTime()).ifPresent(flg::setArrivalTime);
            Optional.ofNullable(flight.getDepartureTime()).ifPresent(flg::setDepartureTime);
            Optional.ofNullable(flight.getPartnerLogo()).ifPresent(flg::setPartnerLogo);
            Optional.ofNullable(flight.getPlaneName()).ifPresent(flg::setPlaneName);
            Optional.ofNullable(flight.getSlug()).ifPresent(flg::setSlug);
            Optional.ofNullable(flight.getAmount()).ifPresent(flg::setAmount);

            flightsRepository.save(flg);

        });
        return flightData.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @Override
    public ResponseEntity<Object> deleteFlight(String id) {
        Optional<Flight> flightData = flightsRepository.findById(id);
        if (flightData.isPresent()) {
            try {
                flightsRepository.deleteById(id);
                return ResponseEntity.ok(new MessageResponse("Flight deleted successfully"));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Flight not found"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Flight not found"));
        }
    }

    @Override
    public ResponseEntity<List<String>> getUniqueAirlineName() {
        List<String> airlineNameList = flightsRepository.findAll().stream()
                .map(Flight::getAirlineName)
                .distinct()
                .toList();
        return ok(airlineNameList
                .stream()
                .sorted()
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Object> addReview(String id, Reviews review) {
        Optional<Flight> flightData = flightsRepository.findById(id);
        if (flightData.isPresent()) {
            Flight flight = flightData.get();
            if (flight.getReviews() == null) {
                flight.setReviews(new ArrayList<>());
            }
            flight.getReviews().add(review);
            flightsRepository.save(flight);
            return ResponseEntity.ok(new MessageResponse("Review added successfully"));
        } else {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Flight not found"));
        }
    }



    @Override
    public ResponseEntity<Object> filter(String maxPrice, String minPrice,
                                         String airLine, String rating, String sort,
                                         String departureTime, String arrivalTime) {
        int minPriceInt = Integer.parseInt(minPrice);
        int maxPriceInt = Integer.parseInt(maxPrice);
        double ratingDouble = Double.parseDouble(rating);


        List<Flight> flights = flightsRepository.filter(minPriceInt,
                maxPriceInt, airLine, ratingDouble,
                LocalDateTime.parse(departureTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                LocalDateTime.parse(arrivalTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME));

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
