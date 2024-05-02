package com.spring.jwt.mongodb.controllers.user;

import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.models.user.User;
import com.spring.jwt.mongodb.models.user.RecentSearch;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import com.spring.jwt.mongodb.services.user.UserService;
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

    UserService userService;

        @PostMapping("/recentSearch")
        public ResponseEntity<Object> recentSearch(@RequestBody RecentSearch recentSearch) {
            return userService.recentSearch(recentSearch);
        }

    @GetMapping("{id}/recentSearch")
    public ResponseEntity<List<RecentSearch>> getRecentSearch(@PathVariable String id) {
        return userService.getRecentSearch(id);
    }


}
