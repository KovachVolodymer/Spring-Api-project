package com.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@Document(collection = "flights")
public class Flights {

    @Id
    private String id;

    @Indexed(unique = true)
    private Integer flightId;

    private String alt;

    private String geolocation;
    private String price;
    private String duration;
    private String abbreviation;
    private String rating;
    private String airlineName;
    private String departureTime;
    private String arrivalTime;
    private String partnerName;
    private String fromArrive;
    private String toArrive;

    private String photo;
    private String airlineLogo;
    private String partnerLogo;

    private List<String> advantages;
    private List<Reviews> reviewsList;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Flights flights = (Flights) obj;
        return Objects.equals(flightId, flights.flightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId);
    }


}
