package com.spring.jwt.mongodb.models.subModels;

import com.spring.jwt.mongodb.models.Hotel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
@Setter
public class Room {
    @Id
    private String id;

    private String hotelId;

    private String photo;
    private String name;
    private Integer doubleBeds;
    private Integer singleBeds;
    private Integer price;

}
