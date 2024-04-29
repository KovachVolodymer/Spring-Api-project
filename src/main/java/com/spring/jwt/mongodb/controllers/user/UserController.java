package com.spring.jwt.mongodb.controllers.user;


import com.spring.jwt.mongodb.models.user.Card;
import com.spring.jwt.mongodb.models.user.User;
import com.spring.jwt.mongodb.services.UserDetailsImpl;
import com.spring.jwt.mongodb.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserController{

    @Autowired
    UserService userService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<Object> getMe(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUser(userDetails.getId());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PatchMapping("/me")
    public ResponseEntity<Object> updateMe(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody User user) {
        return userService.updateUser(userDetails.getId(), user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @PostMapping("/favoriteFlight")
    public ResponseEntity<Object> favoriteFlight(@RequestBody Map<String, String> body) {
        return userService.favoriteFlight(body);
    }

    @DeleteMapping("/{userId}/favoriteFlight/{flightId}")
    public ResponseEntity<Object> deleteFavoriteFlight(@PathVariable String flightId, @PathVariable String userId) {
        return userService.deleteFavoriteFlight(flightId, userId);
    }


    @DeleteMapping("/{userId}/favoriteHotel/{hotelId}")
    public ResponseEntity<Object> deleteFavoriteHotel(@PathVariable String hotelId, @PathVariable String userId) {
        return userService.deleteFavoriteHotel(hotelId, userId);
    }


    @PostMapping("/favoriteHotel")
    public ResponseEntity<Object> favoriteHotel(@RequestBody Map<String, String> body) {
        return userService.favoriteHotel(body);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/card")
    public ResponseEntity<Object> addCard(@RequestBody Card card, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.addCard(card, userDetails.getId());
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/card/{id}")
    public ResponseEntity<Object> deleteCard(@PathVariable String id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.deleteCard(id, userDetails.getId());
    }


}

