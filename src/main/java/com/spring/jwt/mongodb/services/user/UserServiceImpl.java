package com.spring.jwt.mongodb.services.user;

import com.spring.jwt.mongodb.models.Flight;
import com.spring.jwt.mongodb.models.hotel.Hotel;
import com.spring.jwt.mongodb.models.hotel.OrderRoom;
import com.spring.jwt.mongodb.models.user.Card;
import com.spring.jwt.mongodb.models.user.RecentSearch;
import com.spring.jwt.mongodb.models.user.User;
import com.spring.jwt.mongodb.payload.cvc.CVVEncryptionService;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.FlightsRepository;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

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
            response.put("cards", userData.getCards());
            response.put("orderRooms", userData.getOrderRooms());
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
            if (user.getCards().isEmpty() || user.getCards().stream().noneMatch(card -> card.getId().equals(id))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Card not found"));
            }
            user.getCards().removeIf(card -> card.getId().equals(id));

            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Card is deleted"));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("User not found"));
    }

    @Override
    public ResponseEntity<Object> recentSearch(RecentSearch recentSearch) {
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

    @Override
    public ResponseEntity<List<RecentSearch>> getRecentSearch(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            User userData = user.get();
            List<RecentSearch> recentSearchList = userData.getRecentSearch();
            return ResponseEntity.ok(recentSearchList);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @Override
    public ResponseEntity<Object> orderRoom(OrderRoom orderRoom, String userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        User user = userOptional.get();

        // Перевірка наявності готелю
        if (!hotelsRepository.existsById(orderRoom.getHotelId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Hotel not found"));
        }

        Hotel hotel = hotelsRepository.findById(orderRoom.getHotelId()).get();

        // Перевірка наявності кімнати
        if (hotel.getRooms().stream().noneMatch(room -> room.getId().equals(orderRoom.getRoomId()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Room not found"));
        }

        // Перевірка наявності картки
        boolean cardExists = user.getCards().stream().anyMatch
                (card -> card.getId().equals(orderRoom.getCardId()));
        if (!cardExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Card not found"));
        }

        // Додавання замовлення кімнати до користувача і збереження змін
        user.getOrderRooms().add(orderRoom);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Room ordered"));
    }


}
