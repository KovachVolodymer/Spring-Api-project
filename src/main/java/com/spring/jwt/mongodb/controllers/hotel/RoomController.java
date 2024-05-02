package com.spring.jwt.mongodb.controllers.hotel;

import com.spring.jwt.mongodb.models.hotel.Room;
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

    @PutMapping("{id}/room/{roomId}")
    public ResponseEntity<Object> updateRoom(@RequestBody Room room, @PathVariable String id, @PathVariable String roomId) {
        return hotelService.updateRoom(room, id, roomId);
    }

    @PatchMapping("{id}/room/{roomId}")
    public ResponseEntity<Object> patchRoom(@RequestBody Room room, @PathVariable String id, @PathVariable String roomId) {
        return hotelService.patchRoom(room, id, roomId);
    }

    @DeleteMapping("{id}/room/{roomId}")
    public ResponseEntity<Object> deleteRoom(@PathVariable String id, @PathVariable String roomId) {
        return hotelService.deleteRoom(id, roomId);
    }

}
