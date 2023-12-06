package com.spring.jwt.mongodb.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Favorites {
    @Id
    private String id;


    private Integer hotelId;
    private Integer flightId;
    private List<Hotels> hotelsList = new ArrayList<>();
    private List<Flights> flightsList = new ArrayList<>();

}
