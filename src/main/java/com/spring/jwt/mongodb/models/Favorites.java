package com.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Favorites {

    private String hotelId_or_FlightId;
    private boolean isHotel;

}
