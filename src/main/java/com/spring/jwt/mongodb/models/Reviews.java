package com.spring.jwt.mongodb.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
public class Reviews {
    private static final AtomicLong idCounter = new AtomicLong();
    @Id
    private Long id;

    private String photo;
    private String name;
    private Double rating;
    private String comment;

    public Reviews() {
        this.id = idCounter.incrementAndGet();
    }

}
