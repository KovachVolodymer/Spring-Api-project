package com.spring.jwt.mongodb.models;

import com.spring.jwt.mongodb.models.subModels.Reviews;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "flights")
public class Flight {

    @Id
    private String id;


    private Integer price;
    private Integer numberOfSeats;
    private Double rating;

    private String alt;
    private String geolocation;
    private String duration;
    private String abbreviation;
    private String airlineName;
    private String partnerName;
    private String planeName;
    private String fromArrive;
    private String toArrive;
    private String slug;

    private Date departureDate;
    private Date arrivalDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;


    private String photo;
    private String airlineLogo;
    private String partnerLogo;

    private List<String> advantages;
    private List<Reviews> reviews;

}
