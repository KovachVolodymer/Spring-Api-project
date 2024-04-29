package com.spring.jwt.mongodb.controllers.user;

import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.models.user.User;
import com.spring.jwt.mongodb.models.user.RecentSearch;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class RecentSearchController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HotelsRepository hotelsRepository;

        @PostMapping("/recentSearch")
        public ResponseEntity<Object> recentSearch(@RequestBody RecentSearch recentSearch) {
            Optional<User> user = userRepository.findById(recentSearch.getUserId());
            Optional<Hotel> hotel = hotelsRepository.findById(recentSearch.getHotelId());
            if (user.isPresent() && hotel.isPresent()) {
                User userData = user.get();
                Hotel hotelData = hotel.get();

                recentSearch.setAlt(hotelData.getAlt());
                recentSearch.setPhoto(hotelData.getPhoto());
                recentSearch.setCity(hotelData.getLocation());


                userRepository.save(userData);

                return ResponseEntity.ok(new MessageResponse("Recent search added"));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("User or hotel not found"));
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
