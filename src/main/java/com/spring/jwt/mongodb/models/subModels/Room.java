package com.spring.jwt.mongodb.models.subModels;

import com.spring.jwt.mongodb.models.Hotel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Room {
    private String id;

    private String hotelId;
    private String name;
    private Integer numberOfSeats;
}
