package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.User;
import com.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addFavorites")
    public ResponseEntity<User> addFavorites(String id, String hotelId, boolean isHotel) {
    return null;
    }



}
