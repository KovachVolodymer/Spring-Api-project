package com.spring.jwt.mongodb.models;

import com.spring.jwt.mongodb.models.subModels.Reviews;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "hotels")
public class Hotel {

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
    private List<Reviews> reviews;



}
