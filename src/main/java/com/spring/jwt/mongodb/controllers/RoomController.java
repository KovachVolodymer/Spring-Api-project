package com.spring.jwt.mongodb.controllers;

import com.spring.jwt.mongodb.models.Room;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.services.hotel.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/hotels")
public class RoomController {

    @Autowired
    HotelService hotelService;

    @PostMapping("{id}/room")
    public ResponseEntity<Object> addRoom(@RequestBody Room room, @PathVariable String id) {
        return hotelService.addRoom(room, id);
    }

    @DeleteMapping("{id}/room/{roomId}")
    public ResponseEntity<Object> deleteRoom(@PathVariable String id, @PathVariable String roomId) {
        return hotelService.deleteRoom(id, roomId);
    }

}
