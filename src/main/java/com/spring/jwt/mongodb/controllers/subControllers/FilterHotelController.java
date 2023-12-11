package com.spring.jwt.mongodb.controllers.subControllers;

import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hotels")
public class FilterHotelController {

    @Autowired
    HotelsRepository hotelsRepository;

    @GetMapping("/price")
        public ResponseEntity<Object> filterByPrice(@RequestParam Integer maxPrice) {
        List<Hotel> hotels= hotelsRepository.findByPriceLessThanEqual(maxPrice);
        return ResponseEntity.ok(hotels);
    }
}
