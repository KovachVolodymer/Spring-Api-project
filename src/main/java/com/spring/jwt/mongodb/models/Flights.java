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

    private String flightName;
    private String flightFrom;
    private String flightTo;
    private String flightDate;
    private String flightTime;
    private String flightPrice;
    private String flightDuration;
    private String flightReviews;
    private String flightClass;
    private String flightSeats;
    private String flightAirlineLogo;
    private List<String> flightAdvantages;
    private List<String> flightPhotos;


}
