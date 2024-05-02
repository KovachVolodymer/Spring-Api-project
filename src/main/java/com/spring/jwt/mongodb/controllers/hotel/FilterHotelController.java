package com.spring.jwt.mongodb.controllers.hotel;

import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.services.hotel.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/hotels")
public class FilterHotelController {
    @Autowired
    HotelService hotelService;

    @GetMapping("/filter")
        public ResponseEntity<Object> filterByPrice(@RequestParam (name = "maxPrice" ,defaultValue = "500") String maxPrice,
                                                    @RequestParam (name = "minPrice", defaultValue = "0") String minPrice,
                                                    @RequestParam (name = "rating", defaultValue = "0") String rating,
                                                    @RequestParam (name = "advantages",defaultValue ="") List<String> advantages,
                                                    @RequestParam (name = "sort",defaultValue ="") String sort) {

       return hotelService.filterByPrice(maxPrice, minPrice, rating, advantages, sort);
    }

}
