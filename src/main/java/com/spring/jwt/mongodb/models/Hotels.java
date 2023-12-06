package com.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@Document(collection = "hotels")
public class Hotels {

    @Id
    private String id;

    private String name;
    private String slug;
    private Integer price;
    private String location;
    private Integer starRating;
    private String description;

    private String photo;

    private List<String> advantages;
    private List<Reviews> reviewsList;



}
