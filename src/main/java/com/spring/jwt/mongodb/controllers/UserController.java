package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Favorites;
import com.spring.jwt.mongodb.models.Hotels;
import com.spring.jwt.mongodb.models.User;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import com.spring.jwt.mongodb.repository.HotelsRepository;
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
        UserRepository userRepository;

        @Autowired
        HotelsRepository hotelsRepository;

        @Autowired
        FlightsRepository flightsRepository;


}


