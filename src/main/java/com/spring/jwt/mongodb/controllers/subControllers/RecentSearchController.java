package com.spring.jwt.mongodb.controllers.subControllers;

import com.spring.jwt.mongodb.models.User;
import com.spring.jwt.mongodb.models.subModels.RecentSearch;
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

    @PostMapping("/recentSearch{id}")
    public ResponseEntity<String> recentSearch(@PathVariable String id, @RequestBody RecentSearch recentSearch){
        Optional<User> user=userRepository.findById(id);
        if(user.isPresent())
        {
            User userData=user.get();
            List<RecentSearch> recentSearchList=userData.getRecentSearches();
            recentSearchList.add(recentSearch);
            userData.setRecentSearches(recentSearchList);
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
            List<RecentSearch> recentSearchList=userData.getRecentSearches();
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
            List<RecentSearch> recentSearchList=userData.getRecentSearches();
            recentSearchList.removeIf(recentSearch1 -> recentSearch1.getId().equals(recentSearch.getId()));
            userData.setRecentSearches(recentSearchList);
            userRepository.save(userData);
            return ResponseEntity.ok("Recent search deleted successfully");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found");
        }
    }
}
