package com.spring.jwt.mongodb.controllers.subControllers;

import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.models.User;
import com.spring.jwt.mongodb.models.subModels.RecentSearch;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class RecentSearchController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HotelsRepository hotelsRepository;

        @PostMapping("/recentSearch")
        public ResponseEntity<String> recentSearch(@RequestBody RecentSearch recentSearch) {
            Optional<User> user = userRepository.findById(recentSearch.getUserId());
            Optional<Hotel> hotel = hotelsRepository.findById(recentSearch.getHotelId());
            if (user.isPresent() && hotel.isPresent()) {
                User userData = user.get();
                Hotel hotelData = hotel.get();

                recentSearch.setAlt(hotelData.getAlt());
                recentSearch.setPhoto(hotelData.getPhoto());
                recentSearch.setCity(hotelData.getLocation());


                userRepository.save(userData);

                return ResponseEntity.ok("Recent search added");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found or hotel not found");
            }
        }

    @GetMapping("{id}/recentSearch")
    public ResponseEntity<List<RecentSearch>> getRecentSearch(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userData = user.get();
            List<RecentSearch> recentSearchList = userData.getRecentSearch();
            return ResponseEntity.ok(recentSearchList);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }


}
