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
import java.util.Optional;

@RestController
@RequestMapping("/api/hotels")
public class FilterHotelController {

    @Autowired
    HotelsRepository hotelsRepository;

    @GetMapping("/price")
        public ResponseEntity<Object> filterByPrice(@RequestParam (name = "maxPrice" ,defaultValue = "0") Integer maxPrice,
                                                    @RequestParam (name = "minPrice", defaultValue = "0")  Integer minPrice ) {


        List<Hotel> hotels= Optional.of(maxPrice)
                .filter(max -> max > 0)
                .map(max -> hotelsRepository.findByPriceBetween(minPrice, max))
                .orElseGet(() -> hotelsRepository.findByPriceGreaterThanEqual(minPrice));

        return hotels.isEmpty()
                ? ResponseEntity.badRequest().body("No hotels found")
                : ResponseEntity.ok(hotels);
    }
}
