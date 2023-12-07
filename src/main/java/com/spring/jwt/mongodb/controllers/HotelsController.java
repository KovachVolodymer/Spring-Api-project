package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.models.User;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/hotels")
public class HotelsController {

    @Autowired
    HotelsRepository hotelsRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<List<Map<String, Object>>> hotels() {
        List<Hotel> hotelsList = hotelsRepository.findAll();

        Set<String> uniqueAdvantages = hotelsList.stream()
                .flatMap(hotel -> hotel.getAdvantages().stream())
                .collect(Collectors.toSet());

        List<Map<String, Object>> hotelMaps = hotelsList.stream()
                .map(hotel -> {
                    Map<String, Object> hotelMap = new HashMap<>();
                    hotelMap.put("hotelId", hotel.getId());
                    hotelMap.put("photo", hotel.getPhoto());
                    hotelMap.put("name", hotel.getName());
                    hotelMap.put("rating", hotel.getStarRating());
                    hotelMap.put("price", hotel.getPrice());
                    hotelMap.put("location", hotel.getLocation());
                    hotelMap.put("reviews", hotel.getReviewsList());
                    return hotelMap;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("uniqueAdvantages", new ArrayList<>(uniqueAdvantages));
        response.put("hotels", hotelMaps);

        return ok(Collections.singletonList(response));
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotel> hotelById(@PathVariable String id) {
        Optional<Hotel> hotelData = hotelsRepository.findById(id);
        return hotelData.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @PostMapping("")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotel> addHotel(@RequestBody Hotel hotel) {
        if (hotel.getId() != null && hotelsRepository.existsById(hotel.getId())) {
            // If a hotel with the specified id already exists, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            // If no id is specified or there is no existing hotel with the given id, save the new hotel
            String slug = hotel.getName().toLowerCase()
                    .replaceAll(" ", "_")
                    .replaceAll("[^a-z0-9_-]", "");
            hotel.setSlug(slug);
            Hotel newHotel = hotelsRepository.save(hotel);
            return ResponseEntity.status(HttpStatus.CREATED).body(newHotel);
        }
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotel> updateHotel(@PathVariable String id, @RequestBody Hotel hotel) {
        Optional<Hotel> existingHotel = hotelsRepository.findById(id);
        if (existingHotel.isPresent()) {
            Hotel hotelData = existingHotel.get();
            hotelData.setName(hotel.getName());
            hotelData.setPrice(hotel.getPrice());
            hotelData.setLocation(hotel.getLocation());
            hotelData.setStarRating(hotel.getStarRating());
            hotelData.setDescription(hotel.getDescription());
            hotelData.setPhoto(hotel.getPhoto());
            hotelData.setAdvantages(hotel.getAdvantages());
            hotelData.setReviewsList(hotel.getReviewsList());
            Hotel updatedHotel = hotelsRepository.save(hotelData);
            return ResponseEntity.ok(updatedHotel);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotel(@PathVariable String id) {
        Optional<Hotel> hotelData = hotelsRepository.findById(id);
        if (hotelData.isPresent()) {
            try {
                hotelsRepository.deleteById(id);
                return ResponseEntity.ok("Hotel was successfully deleted.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Hotel not found");
            }
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Hotel not found");
        }
    }

    @PatchMapping("/{id}")
    //@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Hotel> patchHotel(@PathVariable String id, @RequestBody Hotel hotel) {

        Optional<Hotel> optionalHotel = hotelsRepository.findById(id);
        optionalHotel.ifPresent(h -> {
            Optional.ofNullable(hotel.getName()).ifPresent(h::setName);
            Optional.ofNullable(hotel.getLocation()).ifPresent(h::setLocation);
            Optional.ofNullable(hotel.getPrice()).ifPresent(h::setPrice);
            Optional.ofNullable(hotel.getPhoto()).ifPresent(h::setPhoto);
            Optional.ofNullable(hotel.getStarRating()).ifPresent(h::setStarRating);
            Optional.ofNullable(hotel.getAdvantages()).ifPresent(h::setAdvantages);
            hotelsRepository.save(h);
        });
        return optionalHotel.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @PostMapping("/favoriteHotel/{hotelId}/{userId}")
    public ResponseEntity<String> favoriteHotel(@PathVariable String hotelId, @PathVariable String userId) {
        Optional<Hotel> hotelData = hotelsRepository.findById(hotelId);
        Optional<User> userData = userRepository.findById(userId);

        if (hotelData.isPresent() && userData.isPresent()) {
            Hotel hotel = hotelData.get();
            User user = userData.get();
            user.getFavoritesListHotels().add(hotel);
            userRepository.save(user);
            return ResponseEntity.ok("Hotel added to favorites successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Hotel or user not found");
        }

    }

    @DeleteMapping("/favoriteHotel/{hotelId}/{userId}")
    public ResponseEntity<String> deleteFavoriteHotel(@PathVariable String hotelId, @PathVariable String userId) {
        Optional<Hotel> hotelData = hotelsRepository.findById(hotelId);
        Optional<User> userData = userRepository.findById(userId);

        if (hotelData.isPresent() && userData.isPresent()) {
            Hotel hotel = hotelData.get();
            User user = userData.get();
            user.getFavoritesListHotels().remove(hotel);
            userRepository.save(user);
            return ResponseEntity.ok("Hotel removed from favorites successfully");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Hotel or user not found");
        }

    }



}
