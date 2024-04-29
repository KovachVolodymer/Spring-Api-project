package com.spring.jwt.mongodb.models;

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



    private String photo;
    private String name;
    private Integer doubleBeds;
    private Integer singleBeds;
    private Integer price;
    private Integer amount;

}
