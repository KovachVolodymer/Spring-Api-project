package com.spring.jwt.mongodb.services.hotel;

import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.models.Reviews;
import com.spring.jwt.mongodb.models.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface HotelService {

    ResponseEntity<List<Map<String, Object>>> hotels();

    ResponseEntity<Hotel> hotelById(String id);

    ResponseEntity<Hotel> addHotel(Hotel hotel);

    ResponseEntity<Hotel> updateHotel(String id, Hotel hotel);

    ResponseEntity<Object> deleteHotel(String id);

    ResponseEntity<Hotel> patchHotel(String id, Hotel hotel);

    ResponseEntity<Object> getUniqueAdvantages();

    ResponseEntity<Object> addReview(String id, Reviews review);

    ResponseEntity<Object> filterByPrice(String maxPrice, String minPrice, String rating,
                                         List<String> advantages, String sort);

    ResponseEntity<Object> addRoom(Room room,String id);

    ResponseEntity<Object> deleteRoom(String id, String roomId);
}
