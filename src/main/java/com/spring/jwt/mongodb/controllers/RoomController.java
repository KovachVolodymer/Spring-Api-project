package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Room;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
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
        return ResponseEntity.ok().body(new MessageResponse("Room added"));
    }

}
