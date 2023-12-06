package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.*;
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
            response.put("dataBirth", userData.getDataBirth());
            response.put("recentSearches", userData.getRecentSearches());
            response.put("favoritesList", userData.getFavoritesList());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

    }


    @PostMapping("/favorites/{id}")
    public ResponseEntity<String> addFavoriteHotel(@PathVariable String id,@RequestBody Favorites favorites) {
        Optional<User> userOptional = userRepository.findById(id);

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
            if (favorites.getId() != null) {
                Optional<Hotels> hotelOptional = hotelsRepository.findById(favorites.getId());

                if (hotelOptional.isPresent() && !userFavorites.getHotelsList().contains(hotelOptional.get())) {
                    userFavorites.getHotelsList().add(hotelOptional.get());
                } else {
                    // Готель вже існує в списку або не знайдено
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Hotel already exists or not found");
                }
            }

            // Перевірити, чи існує flightsId перед викликом intValue()
            if (favorites.getFlightId() != null) {
                Optional<Flights> flightOptional = flightsRepository.findById(favorites.getFlightId());

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
        String userId = favorites.getId();
        String hotelId = favorites.getId();
        String flightId = favorites.getFlightId();

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Отримати або створити об'єкт Favorites для користувача
            List<Favorites> userFavoritesList = user.getFavoritesList();

            if (!userFavoritesList.isEmpty()) {
                Favorites userFavorites = userFavoritesList.get(0);

                // Видалити готель зі списку
                if (hotelId != null) {
                    userFavorites.getHotelsList().removeIf(hotel -> hotel.getId().equals(hotelId));
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
        Optional<User> userOptional = userRepository.findById(user.getId());

        if (userOptional.isPresent()) {
            User u = userOptional.get();
            Optional.ofNullable(user.getUsername()).ifPresent(u::setUsername);
            Optional.ofNullable(user.getAddress()).ifPresent(u::setAddress);
            Optional.ofNullable(user.getPhone()).ifPresent(u::setPhone);
            Optional.ofNullable(user.getDataBirth()).ifPresent(u::setDataBirth);
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
            if (user.getNewEmail() == null) {
                u.setEmail(u.getEmail());
            } else if (userRepository.existsByEmail(user.getNewEmail())) {
                u.setEmail(u.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email name already exists");
            } else {
                u.setEmail(user.getNewEmail());
            }


            userRepository.save(u);
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found");
        }
    }

    @PostMapping("/recentSearch")
    public ResponseEntity<String> recentSearch(@RequestBody RecentSearch recentSearch){
        Optional<User> user=userRepository.findById(recentSearch.getUserId());
        if(user.isPresent())
        {
            User userData=user.get();
            List<RecentSearch> recentSearchList=userData.getRecentSearches();
            recentSearchList.add(recentSearch);
            userData.setRecentSearches(recentSearchList);
            userRepository.save(userData);
            return ResponseEntity.ok("Recent search added successfully");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found");
        }
    }

    @GetMapping("/recentSearch/{id}")
    public ResponseEntity<List<RecentSearch>> getRecentSearch(@PathVariable String id){
        Optional<User> user=userRepository.findById(id);
        if(user.isPresent())
        {
            User userData=user.get();
            List<RecentSearch> recentSearchList=userData.getRecentSearches();
            return ResponseEntity.ok(recentSearchList);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @DeleteMapping("/recentSearch")
    public ResponseEntity<String> deleteRecentSearch(@RequestBody RecentSearch recentSearch){
        Optional<User> user=userRepository.findById(recentSearch.getUserId());
        if(user.isPresent())
        {
            User userData=user.get();
            List<RecentSearch> recentSearchList=userData.getRecentSearches();
            recentSearchList.removeIf(recentSearch1 -> recentSearch1.getId().equals(recentSearch.getId()));
            userData.setRecentSearches(recentSearchList);
            userRepository.save(userData);
            return ResponseEntity.ok("Recent search deleted successfully");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found");
        }
    }



}


