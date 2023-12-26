package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.*;
import com.spring.jwt.mongodb.models.subModels.RecentSearch;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HotelsRepository hotelsRepository;

    @Autowired
    FlightsRepository flightsRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            User userData = user.get();
            response.put("id", userData.getId());
            response.put("username", userData.getUsername());
            response.put("email", userData.getEmail());
            response.put("avatar", userData.getAvatar());
            response.put("phone", userData.getPhone());
            response.put("address", userData.getAddress());
            response.put("dataBirth", userData.getBirthday());
            response.put("recentSearch", userData.getRecentSearch());
            response.put("favoritesListHotels", userData.getFavoritesHotels());
            response.put("favoritesListFlights", userData.getFavoritesFlights());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

    }

    @PatchMapping
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        Optional<User> userOptional = userRepository.findById(user.getId());

        if (userOptional.isPresent()) {
            User u = userOptional.get();
            Optional.ofNullable(user.getUsername()).ifPresent(u::setUsername);
            Optional.ofNullable(user.getAddress()).ifPresent(u::setAddress);
            Optional.ofNullable(user.getPhone()).ifPresent(u::setPhone);
            Optional.ofNullable(user.getBirthday()).ifPresent(u::setBirthday);
            Optional.ofNullable(user.getAvatar()).ifPresent(u::setAvatar);
            Optional.ofNullable(user.getPassword()).ifPresent(p -> u.setPassword(encoder.encode(p)));
            if (user.getUsername() == null) {
                u.setUsername(u.getUsername());
            } else if (userRepository.existsByUsername(user.getUsername())) {
                u.setUsername(u.getUsername());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User name already exists");
            } else {
                u.setUsername(user.getUsername());
            }
            userRepository.save(u);
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found");
        }
    }

    @PostMapping("/favoriteFlight")
    public ResponseEntity<String> favoriteFlight(@RequestBody Map<String, String> body) {
        String flightId = body.get("flightId");
        String userId = body.get("userId");
        Optional<Flight> flightData = flightsRepository.findById(flightId);
        Optional<User> userData = userRepository.findById(userId);

        if (flightData.isPresent() && userData.isPresent()) {
            Flight flight = flightData.get();
            User user = userData.get();
            user.getFavoritesFlights().add(flight);
            userRepository.save(user);
            return ResponseEntity.ok("Flight added to favorites successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Flight or user not found");
        }

    }

    @DeleteMapping("/{userId}/favoriteFlight/{flightId}")
    public ResponseEntity<String> deleteFavoriteFlight(@PathVariable String flightId, @PathVariable String userId) {
        Optional<Flight> flightData = flightsRepository.findById(flightId);
        Optional<User> userData = userRepository.findById(userId);

        if (flightData.isPresent() && userData.isPresent()) {
            Flight flight = flightData.get();
            User user = userData.get();
            user.getFavoritesFlights().remove(flight);
            userRepository.save(user);
            return ResponseEntity.ok("Flight removed from favorites successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Flight or user not found");
        }

    }

    @PostMapping("/favoriteHotel")
    public ResponseEntity<String> favoriteHotel(@RequestBody Map<String, String> body) {
        String hotelId = body.get("hotelId");
        String userId = body.get("userId");

        Optional<Hotel> hotelData = hotelsRepository.findById(hotelId);
        Optional<User> userData = userRepository.findById(userId);

        if (hotelData.isPresent() && userData.isPresent()) {
            Hotel hotel = hotelData.get();
            User user = userData.get();
            user.getFavoritesHotels().add(hotel);
            userRepository.save(user);
            return ResponseEntity.ok("Hotel added to favorites successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Hotel or user not found");
        }

    }

    @DeleteMapping("/{userId}/favoriteHotel/{hotelId}")
    public ResponseEntity<String> deleteFavoriteHotel(@PathVariable String hotelId, @PathVariable String userId) {
        Optional<Hotel> hotelData = hotelsRepository.findById(hotelId);
        Optional<User> userData = userRepository.findById(userId);

        if (hotelData.isPresent() && userData.isPresent()) {
            Hotel hotel = hotelData.get();
            User user = userData.get();
            user.getFavoritesHotels().remove(hotel);
            userRepository.save(user);
            return ResponseEntity.ok("Hotel removed from favorites successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Hotel or user not found");
        }

    }




}


