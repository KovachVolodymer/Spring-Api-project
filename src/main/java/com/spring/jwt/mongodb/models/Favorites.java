package com.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class Favorites {
    private List<String> hotelsID;
    private List<String> flightsID;
}
