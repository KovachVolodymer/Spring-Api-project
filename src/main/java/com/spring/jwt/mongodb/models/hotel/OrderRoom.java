package com.spring.jwt.mongodb.models.hotel;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class OrderRoom {

    private static final AtomicLong idCounter = new AtomicLong();

    @Id
    public String id;

    @NotBlank(message = "Hotel id is required")
    public String hotelId;

    @NotBlank(message = "Room id is required")
    public String roomId;

    public String cardId;

    public Date fromDate;

    public Date toDate;

    public OrderRoom() {
        this.id = String.valueOf(idCounter.incrementAndGet());
    }
}
