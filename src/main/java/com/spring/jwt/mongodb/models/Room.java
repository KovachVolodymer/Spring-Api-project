package com.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
@Getter
@Setter
public class Room {

    private static final AtomicLong idCounter = new AtomicLong();

    @Id
    private String id;


    private String name;
    private String photo;
    private Integer doubleBeds;
    private Integer singleBeds;
    private Integer price;
    private Integer amount;

    public Room() {
        this.id = String.valueOf(idCounter.incrementAndGet());
    }

}
