package com.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "flights")
public class Flights {

    @Id
    private String id;

    @Indexed(unique = true)
    private Integer flightId;

    private String ALT;

    private String geolocation;
    private String price;
    private String duration;
    private String abbreviation;
    private String rating;
    private String airlineName;
    private String departureTime;
    private String arrivalTime;
    private String partnerName;

    private String photo;
    private String airlineLogo;
    private String partnerLogo;

    private List<String> advantages;


}
