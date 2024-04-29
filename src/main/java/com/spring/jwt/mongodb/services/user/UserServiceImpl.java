package com.spring.jwt.mongodb.services.user;

import com.spring.jwt.mongodb.models.Flight;
import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.models.user.Card;
import com.spring.jwt.mongodb.models.user.User;
import com.spring.jwt.mongodb.payload.cvc.CVVEncryptionService;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import com.spring.jwt.mongodb.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    HotelsRepository hotelsRepository;

    @Autowired
    FlightsRepository flightsRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    CVVEncryptionService cvvEncryptionService;



    @Override
    public ResponseEntity<Object> getUser(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            User userData = user.get();
            response.put("id", userData.getId());
            response.put("username", userData.getUsername());
            response.put("email", userData.getEmail());
            response.put("avatar", userData.getAvatar());
            response.put("phone", userData.getPhone());
            response.put("address", userData.getAddress());
            response.put("dataBirth", userData.getBirthday());
            response.put("recentSearch", userData.getRecentSearch());
            response.put("favoritesHotels", userData.getFavoritesHotels());
            response.put("favoritesFlights", userData.getFavoritesFlights());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("User not found"));
        }
    }

    @Override
    public ResponseEntity<Object> updateUser(String id, User user) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User u = userOptional.get();
            Optional.ofNullable(user.getUsername()).ifPresent(u::setUsername);
            Optional.ofNullable(user.getAddress()).ifPresent(u::setAddress);
            Optional.ofNullable(user.getPhone()).ifPresent(u::setPhone);
            Optional.ofNullable(user.getBirthday()).ifPresent(u::setBirthday);
            Optional.ofNullable(user.getAvatar()).ifPresent(u::setAvatar);
            Optional.ofNullable(user.getPassword()).ifPresent(p -> u.setPassword(encoder.encode(p)));
            if (userRepository.existsByEmail(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Email already in use"));
            }
            Optional.ofNullable(user.getEmail()).ifPresent(u::setEmail);

            userRepository.save(u);
            return ResponseEntity.ok(new MessageResponse("User updated successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("User not found"));
        }
    }

    @Override
    public ResponseEntity<Object> favoriteFlight(Map<String, String> body) {
        String flightId = body.get("flightId");
        String userId = body.get("userId");

        Optional<Flight> flightData = flightsRepository.findById(flightId);
        Optional<User> userData = userRepository.findById(userId);

        if (flightData.isPresent() && userData.isPresent()) {
            Flight flight = flightData.get();
            User user = userData.get();
            user.getFavoritesFlights().add(flight);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Flight added to favorites successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Flight or user not found"));
        }
    }

    @Override
    public ResponseEntity<Object> favoriteHotel(Map<String, String> body) {
        String hotelId = body.get("hotelId");
        String userId = body.get("userId");

        Optional<Hotel> hotelData = hotelsRepository.findById(hotelId);
        Optional<User> userData = userRepository.findById(userId);

        if (hotelData.isPresent() && userData.isPresent()) {
            Hotel hotel = hotelData.get();
            User user = userData.get();
            user.getFavoritesHotels().add(hotel);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Hotel added to favorites successfully"));
        } else {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Hotel or user not found"));
        }
    }

    @Override
    public ResponseEntity<Object> deleteFavoriteFlight(String flightId, String userId) {
        Optional<Flight> flightData = flightsRepository.findById(flightId);
        Optional<User> userData = userRepository.findById(userId);

        if (flightData.isPresent() && userData.isPresent()) {
            Flight flight = flightData.get();
            User user = userData.get();
            user.getFavoritesFlights().remove(flight);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Flight removed from favorites successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Flight or user not found"));
        }
    }

    @Override
    public ResponseEntity<Object> deleteFavoriteHotel(String hotelId, String userId) {
        Optional<Hotel> hotelData = hotelsRepository.findById(hotelId);
        Optional<User> userData = userRepository.findById(userId);

        if (hotelData.isPresent() && userData.isPresent()) {
            Hotel hotel = hotelData.get();
            User user = userData.get();
            user.getFavoritesHotels().remove(hotel);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Hotel removed from favorites successfully"));
        } else {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Hotel or user not found"));
        }
    }

    @Override
    public ResponseEntity<Object> addCard(Card card, String id) {
        if (card == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Card is null"));
        }
        if (card.getExpiryDate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Expiry date is required"));
        }

        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            User user = userData.get();

            String input = card.getExpiryDate();
            String[] parts = input.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            // Форматування місяця/року у потрібний формат
            String formattedDate = String.format("%02d/%02d", month, year % 100);

            card.setCvc(cvvEncryptionService.encryptCVV(card.getCvc()));

            card.setExpiryDate(formattedDate);
            user.getCards().add(card);
            userRepository.save(user);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Card is add"));
    }

    @Override
    public ResponseEntity<Object> deleteCard(String idCard, String id) {
        Optional<User> userData = userRepository.findById(id);
        if (userData.isPresent()) {
            User user = userData.get();
            if(user.getCards().isEmpty() || user.getCards().stream().noneMatch(card -> card.getId().equals(id))){
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Card not found"));
            }
            user.getCards().removeIf(card -> card.getId().equals(id));

            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Card is deleted"));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("User not found"));
    }
}