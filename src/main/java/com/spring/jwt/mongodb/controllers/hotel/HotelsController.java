package com.spring.jwt.mongodb.controllers.hotel;

import com.spring.jwt.mongodb.models.hotel.Hotel;
import com.spring.jwt.mongodb.models.Reviews;
import com.spring.jwt.mongodb.services.hotel.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/hotels")
public class HotelsController {
    @Autowired
    HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> hotels() {
        return hotelService.hotels();
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotel> hotelById(@PathVariable String id) {
        return hotelService.hotelById(id);
    }

    @PostMapping
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotel> addHotel(@RequestBody Hotel hotel) {
        return hotelService.addHotel(hotel);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotel> updateHotel(@PathVariable String id, @RequestBody Hotel hotel) {
        return hotelService.updateHotel(id, hotel);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteHotel(@PathVariable String id) {
        return hotelService.deleteHotel(id);
    }

    @PatchMapping("/{id}")
    // @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotel> patchHotel(@PathVariable String id, @RequestBody Hotel hotel) {
        return hotelService.patchHotel(id, hotel);
    }

    @GetMapping("/advantages")
    public ResponseEntity<Object> getUniqueAdvantages() {
        return hotelService.getUniqueAdvantages();
    }

    @PostMapping("/review/{id}")
    public ResponseEntity<Object> addReview(@PathVariable String id,@RequestBody Reviews review) {
        return hotelService.addReview(id, review);
    }


}
