package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.repository.HotelsRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/hotels")
public class HotelsController {
    HotelsRepository hotelsRepository;
    @GetMapping("/allHotels")
    public String hotels(){
        return hotelsRepository.findAll().toString();
    }


}
