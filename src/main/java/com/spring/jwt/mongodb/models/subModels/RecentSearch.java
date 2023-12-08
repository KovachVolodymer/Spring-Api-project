package com.spring.jwt.mongodb.models.subModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
public class RecentSearch {

    @Id
    private String id;

    private String userId;
    private String hotelId;

    private Integer places;

    private String alt;
    private String city;
    private String photo;

}
