package com.spring.jwt.mongodb.models.flight;

import com.spring.jwt.mongodb.models.Reviews;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "flights")
public class Flight {

    @Id
    private String id;


    private Integer price;
    private Integer places;
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

    private Integer amount;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private String photo;
    private String airlineLogo;
    private String partnerLogo;

    private List<String> advantages;
    private List<Reviews> reviews;


}
