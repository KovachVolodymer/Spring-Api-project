package com.spring.jwt.mongodb.models.hotel;

import com.spring.jwt.mongodb.models.Reviews;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@Document(collection = "hotel")
public class Hotel {

    @Id
    private String id;

    private String name;
    private String alt;
    private String slug;
    private String location;
    private String description;

    private Double rating;

    private Integer price;

    private String photo;

    private String city;

    private List<String> advantages;

    private List<Reviews> reviews;

    private List<Room> rooms = new ArrayList<>();

    public Hotel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return Objects.equals(id, hotel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
