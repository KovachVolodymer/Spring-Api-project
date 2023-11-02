package com.spring.jwt.mongodb.models;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "hotels")
public class Hotels {

    @Id
    private String id;

    @Indexed(unique = true)
    private int hotelId;

    private String name;
    private String address;
    private String starRating;
    private String description;
    private String photo;
    private String pricePerNight;
    private List<String> advantages;

}
