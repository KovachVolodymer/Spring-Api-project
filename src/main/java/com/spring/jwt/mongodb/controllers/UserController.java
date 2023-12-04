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
import org.springframework.security.crypto.password.PasswordEncoder;
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

        @Autowired
        PasswordEncoder encoder;

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
                        List<Favorites> userFavoritesList = user.getFavoritesList();

                        Favorites userFavorites;
                        if (userFavoritesList.isEmpty()) {
                                userFavorites = new Favorites();
                                userFavoritesList.add(userFavorites);
                        } else {
                                userFavorites = userFavoritesList.get(0);
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

        @DeleteMapping("/favorites")
        public ResponseEntity<String> deleteFavorite(
                @RequestBody Favorites favorites
        ) {
                String userEmail = favorites.getUserEmail();
                Integer hotelId = favorites.getHotelsId();
                Integer flightId = favorites.getFlightsId();

                Optional<User> userOptional = userRepository.findByEmail(userEmail);

                if (userOptional.isPresent()) {
                        User user = userOptional.get();

                        // Отримати або створити об'єкт Favorites для користувача
                        List<Favorites> userFavoritesList = user.getFavoritesList();

                        if (!userFavoritesList.isEmpty()) {
                                Favorites userFavorites = userFavoritesList.get(0);

                                // Видалити готель зі списку
                                if (hotelId != null) {
                                        userFavorites.getHotelsList().removeIf(hotel -> hotel.getHotelId().equals(hotelId));
                                }

                                // Видалити рейс зі списку
                                if (flightId != null) {
                                        userFavorites.getFlightsList().removeIf(flight -> flight.getFlightId().equals(flightId));
                                }

                                // Зберегти користувача
                                userRepository.save(user);

                                return ResponseEntity.ok("Favorite deleted successfully");
                        } else {
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorites not found for the user");
                        }
                } else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found");
                }
        }


       @PatchMapping("")
       public ResponseEntity<String> updateUser(@RequestBody User user) {
               Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

               if (userOptional.isPresent()) {
                       User u = userOptional.get();
                       Optional.ofNullable(user.getUsername()).ifPresent(u::setUsername);
                       Optional.ofNullable(user.getAddress()).ifPresent(u::setAddress);
                       Optional.ofNullable(user.getPhone()).ifPresent(u::setPhone);
                       Optional.ofNullable(user.getDataBirth()).ifPresent(u::setDataBirth);
                       Optional.ofNullable(user.getAvatar()).ifPresent(u::setAvatar);
                       Optional.ofNullable(user.getPassword()).ifPresent(p -> u.setPassword(encoder.encode(p)));
                       if(user.getUsername()==null)
                       {
                               u.setUsername(u.getUsername());
                       }
                       else if(userRepository.existsByUsername(user.getUsername()))
                       {
                               u.setUsername(u.getUsername());
                               return  ResponseEntity.status(HttpStatus.CONFLICT).body("User name already exists");
                       }
                       else {
                               u.setUsername(user.getUsername());
                       }
                       if(user.getNewEmail()==null)
                       {
                               u.setEmail(u.getEmail());
                       } else if (userRepository.existsByEmail(user.getNewEmail())) {
                               u.setEmail(u.getEmail());
                               return ResponseEntity.status(HttpStatus.CONFLICT).body("Email name already exists");
                       }
                       else {
                               u.setEmail(user.getNewEmail());
                       }


                       userRepository.save(u);
                       return ResponseEntity.ok("User updated successfully");
               } else {
                       return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found");
               }
       }










}


