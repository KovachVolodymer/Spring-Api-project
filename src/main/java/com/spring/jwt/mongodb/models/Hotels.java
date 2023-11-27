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

    @Indexed(unique = true)
    private int hotelId;

    private String name;
    private String price;
    private String location;
    private String starRating;
    private String description;

    private String photo;

    private List<String> advantages;
    private List<Reviews> reviewsList;

    public Hotels() {
        this.reviewsList = new ArrayList<>();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Hotels hotels = (Hotels) obj;
        return Objects.equals(hotelId, hotels.hotelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hotelId);
    }

}
