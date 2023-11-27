package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Favorites;
import com.spring.jwt.mongodb.models.Hotels;
import com.spring.jwt.mongodb.models.User;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

        @Autowired
        UserRepository userRepository;

        @Autowired
        HotelsRepository hotelsRepository;

        @Autowired
        FlightsRepository flightsRepository;

        @PostMapping("/addFavorite/{userId}")
        public ResponseEntity<String> addFavoriteHotel(@PathVariable String userId, @RequestBody Favorites newFavorites) {
                try {
                        Optional<User> optionalUser = userRepository.findById(userId);

                        if (optionalUser.isPresent()) {
                                User user = optionalUser.get();
                                List<String> existingHotelsID = user.getFavoritesList()
                                        .stream()
                                        .flatMap(favorites -> favorites.getHotelsID().stream())
                                        .collect(Collectors.toList());

                                existingHotelsID.addAll(newFavorites.getHotelsID());

                                Favorites updatedFavorites = user.getFavoritesList().stream()
                                        .filter(favorites -> !Collections.disjoint(favorites.getHotelsID(), newFavorites.getHotelsID()))
                                        .findFirst()
                                        .orElse(newFavorites);

                                updatedFavorites.getHotelsID().addAll(existingHotelsID);

                                if (!user.getFavoritesList().contains(updatedFavorites)) {
                                        user.getFavoritesList().add(updatedFavorites);
                                }

                                userRepository.save(user);

                                return new ResponseEntity<>("Favorite hotels додано успішно", HttpStatus.CREATED);
                        } else {
                                return new ResponseEntity<>("Користувача з id " + userId + " не знайдено", HttpStatus.NOT_FOUND);
                        }
                } catch (Exception e) {
                        return new ResponseEntity<>("Помилка при додаванні улюблених готелів: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }


}


