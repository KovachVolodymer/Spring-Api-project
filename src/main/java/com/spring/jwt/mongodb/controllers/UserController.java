package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Favorites;
import com.spring.jwt.mongodb.models.User;
import com.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addFavorites/{userId}")
    public ResponseEntity<String> addFavorites(@PathVariable String userId, @RequestBody Favorites request) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User _user = user.get();
            _user.getFavoritesList().add(request);
            userRepository.save(_user);
            return ResponseEntity.ok("Favorites added successfully");
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }



}


