package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Hotels;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/hotels")
public class HotelsController {
    HotelsRepository hotelsRepository;

    public HotelsController(HotelsRepository hotelsRepository){
        this.hotelsRepository = hotelsRepository;
    }
    @GetMapping("/allHotels")
    public ResponseEntity<List<Hotels>> hotels() {
        List<Hotels> hotelList = hotelsRepository.findAll();
        return ResponseEntity.ok(hotelList);
    }


}
