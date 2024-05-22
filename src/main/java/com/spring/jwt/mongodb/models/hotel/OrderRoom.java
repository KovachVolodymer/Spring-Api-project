package com.spring.jwt.mongodb.models.hotel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Setter
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

    @Schema(type = "string", format = "date-time",
            description = "Дата початку бронювання в форматі ISO 8601", example = "2024-05-02")
    public Date fromDate;

    @Schema(type = "string", format = "date-time",
            description = "Дата закінчення бронювання в форматі ISO 8601", example = "2024-05-10")
    public Date toDate;

    public OrderRoom() {
        this.id = String.valueOf(idCounter.incrementAndGet());
    }
}