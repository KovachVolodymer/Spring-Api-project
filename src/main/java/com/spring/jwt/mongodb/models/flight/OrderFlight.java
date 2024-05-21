package com.spring.jwt.mongodb.models.flight;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicLong;

@Setter
@Getter
public class OrderFlight {

    private static final AtomicLong idCounter = new AtomicLong();

    private String id;
    private String flightId;
    private String cardId;

    public OrderFlight() {
        this.id = String.valueOf(idCounter.incrementAndGet());
    }
}
