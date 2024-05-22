package com.spring.jwt.mongodb.models.flight;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import java.util.concurrent.atomic.AtomicLong;

@Setter
@Getter
public class OrderFlight {

    private static final AtomicLong idCounter = new AtomicLong();

    @Id
    private String id;
    private String flightId;
    private String cardId;

    public OrderFlight() {
        this.id = String.valueOf(idCounter.incrementAndGet());
    }
}
