package com.spring.jwt.mongodb.controllers.subControllers;

import com.spring.jwt.mongodb.models.subModels.Room;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
public class RoomController {
    @Autowired
    HotelsRepository hotelsRepository;

    @PostMapping("/rooms")
    public ResponseEntity<Object> addRoom(@RequestBody Room room) {
        hotelsRepository.findById(room.getHotelId()).ifPresent(hotel -> {
            hotel.getRooms().add(room);
            hotelsRepository.save(hotel);
        });
        return ResponseEntity.ok().body("Room added successfully");
    }

}
