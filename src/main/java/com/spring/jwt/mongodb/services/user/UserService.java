package com.spring.jwt.mongodb.services.user;

import com.spring.jwt.mongodb.models.user.Card;
import com.spring.jwt.mongodb.models.user.RecentSearch;
import com.spring.jwt.mongodb.models.user.User;
import com.spring.jwt.mongodb.services.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
import java.util.Map;


public interface UserService {

    ResponseEntity<Object> getUser(String id);

    ResponseEntity<Object> updateUser(String id, User user);

    ResponseEntity<Object> favoriteFlight(Map<String, String> body);

    ResponseEntity<Object> favoriteHotel(Map<String, String> body);

    ResponseEntity<Object> deleteFavoriteFlight(String flightId, String userId);

    ResponseEntity<Object> deleteFavoriteHotel(String hotelId, String userId);

    ResponseEntity<Object> addCard(Card card, String id);

    ResponseEntity<Object> deleteCard(String idCard, String id);

    ResponseEntity<Object> recentSearch(RecentSearch recentSearch);

    ResponseEntity<List<RecentSearch>> getRecentSearch(String id);
}
