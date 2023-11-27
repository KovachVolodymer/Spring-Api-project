package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Favorites;
import com.spring.jwt.mongodb.models.Flights;
import com.spring.jwt.mongodb.models.Hotels;
import com.spring.jwt.mongodb.models.User;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

        @Autowired
        UserRepository userRepository;

        @Autowired
        HotelsRepository hotelsRepository;

        @Autowired
        FlightsRepository flightsRepository;

        @GetMapping("/favorites")
        public ResponseEntity<Map<String, Object>> getFavorites(@RequestParam String email) {
                Optional<User> userOptional = userRepository.findByEmail(email);
                if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        Favorites favorites = user.getFavoritesList().get(0);
                        Map<String, Object> response = new HashMap<>();
                        response.put("hotels", favorites.getHotelsList());
                        response.put("flights", favorites.getFlightsList());
                        return ResponseEntity.ok(response);
                } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
                }

        }


        @PostMapping("/favorites")
        public ResponseEntity<String> addFavoriteHotel(@RequestBody Favorites favorites) {
                Optional<User> userOptional = userRepository.findByEmail(favorites.getUserEmail());

                if (userOptional.isPresent()) {
                        User user = userOptional.get();

                        // Отримати або створити об'єкт Favorites для користувача
                        Favorites userFavorites;
                        if (user.getFavoritesList().isEmpty()) {
                                userFavorites = new Favorites();
                                user.setFavoritesList(Arrays.asList(userFavorites));
                        } else {
                                userFavorites = user.getFavoritesList().get(0);
                        }

                        // Перевірити, чи існує hotelsId перед викликом intValue()
                        if (favorites.getHotelsId()!= null) {
                                Optional<Hotels> hotelOptional = hotelsRepository.findByHotelId(favorites.getHotelsId());

                                if (hotelOptional.isPresent() && !userFavorites.getHotelsList().contains(hotelOptional.get())) {
                                        userFavorites.getHotelsList().add(hotelOptional.get());
                                } else {
                                        // Готель вже існує в списку або не знайдено
                                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Hotel already exists or not found");
                                }
                        }

                        // Перевірити, чи існує flightsId перед викликом intValue()
                        if (favorites.getFlightsId() != null) {
                                Optional<Flights> flightOptional = flightsRepository.findByFlightId(favorites.getFlightsId());

                                if (flightOptional.isPresent() && !userFavorites.getFlightsList().contains(flightOptional.get())) {
                                        userFavorites.getFlightsList().add(flightOptional.get());
                                } else {
                                        // Політ вже існує в списку або не знайдено
                                        return ResponseEntity.status(HttpStatus.CONFLICT).body("Flight already exists or not found");
                                }
                        }

                        // Зберегти користувача
                        userRepository.save(user);

                        return ResponseEntity.ok("Favorites added/updated successfully");
                } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found");
                }
        }




}


