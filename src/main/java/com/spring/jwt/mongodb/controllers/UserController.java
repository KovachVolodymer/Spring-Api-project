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
            response.put("favoritesListHotels", userData.getFavoritesListHotels());
            response.put("favoritesListFlights", userData.getFavoritesListFlights());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
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



}


