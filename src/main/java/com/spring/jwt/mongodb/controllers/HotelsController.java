package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Hotels;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/hotels")
public class HotelsController {

    @Autowired
    HotelsRepository hotelsRepository;


    @GetMapping("/allHotels")
    public ResponseEntity<List<Hotels>> hotels() {
        List<Hotels> hotelList = hotelsRepository.findAll();

        List<Map<String, Object>> hotelMaps = hotelList.stream()
                .map(hotel -> {
                    Map<String, Object> hotelMap = new HashMap<>();
                    hotelMap.put("hotelId", hotel.getHotelId());
                    hotelMap.put("name", hotel.getName());
                    hotelMap.put("location", hotel.getLocation());
                    hotelMap.put("price", hotel.getPrice());
                    hotelMap.put("photo", hotel.getPhoto());
                    hotelMap.put("starRating", hotel.getStarRating());
                    hotelMap.put("advantages", hotel.getAdvantages());
                    return hotelMap;
                })
                .toList();

        return ok(hotelList);
    }

    @GetMapping("/hotel/{id}")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotels> hotelById(@PathVariable int id) {
        Optional<Hotels> hotelData = hotelsRepository.findByHotelId(id);
        return hotelData.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }


    @PostMapping("/addHotel")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotels> addHotel(@RequestBody Hotels hotel) {
        Optional<Hotels> existingHotel = hotelsRepository.findByHotelId(hotel.getHotelId());
        if (existingHotel.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            Hotels newHotel = hotelsRepository.save(hotel);
            return ResponseEntity.ok(newHotel);
        }
    }

    @PutMapping("/updateHotel/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotels> updateHotel(@PathVariable int id, @RequestBody Hotels hotel) {
        Optional<Hotels> hotelData = hotelsRepository.findByHotelId(id);
        if (hotelData.isPresent()) {
            Hotels _hotel = hotelData.get();



            return ResponseEntity.ok(hotelsRepository.save(_hotel));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteHotel/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotel(@PathVariable int id) {
        Optional<Hotels> hotelData = hotelsRepository.findByHotelId(id);
        if (hotelData.isPresent()) {
            try {
                hotelsRepository.deleteByHotelId(id);
                return ResponseEntity.ok("Готель було успішно видалено.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Помилка при видаленні готелю.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Готель з вказаним ID не знайдено.");
        }
    }


}
