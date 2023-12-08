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
public class RecentSearchController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HotelsRepository hotelsRepository;

    @PostMapping("/recentSearch{id}")
    public ResponseEntity<String> recentSearch(@RequestBody RecentSearch recentSearch){
        Optional<User> user=userRepository.findById(recentSearch.getUserId());
        Optional<Hotel> hotel=hotelsRepository.findById(recentSearch.getHotelId());
        if(user.isPresent())
        {
            User userData=user.get();
            List<RecentSearch> recentSearchList=userData.getRecentSearch();
            recentSearchList.add(recentSearch);
            userData.setRecentSearch(recentSearchList);
            userRepository.save(userData);
            return ResponseEntity.ok("Recent search added successfully");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found");
        }
    }

    @GetMapping("/recentSearch/{id}")
    public ResponseEntity<List<RecentSearch>> getRecentSearch(@PathVariable String id){
        Optional<User> user=userRepository.findById(id);
        if(user.isPresent())
        {
            User userData=user.get();
            List<RecentSearch> recentSearchList=userData.getRecentSearch();
            return ResponseEntity.ok(recentSearchList);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @DeleteMapping("/recentSearch{id}")
    public ResponseEntity<String> deleteRecentSearch(@PathVariable String id,@RequestBody RecentSearch recentSearch){
        Optional<User> user=userRepository.findById(id);
        if(user.isPresent())
        {
            User userData=user.get();
            List<RecentSearch> recentSearchList=userData.getRecentSearch();
            recentSearchList.removeIf(recentSearch1 -> recentSearch1.getId().equals(recentSearch.getId()));
            userData.setRecentSearch(recentSearchList);
            userRepository.save(userData);
            return ResponseEntity.ok("Recent search deleted successfully");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found");
        }
    }
}
