package com.spring.jwt.mongodb.models.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.concurrent.atomic.AtomicLong;

@Setter
@Getter
public class Card {
    private static final AtomicLong idCounter = new AtomicLong();

    @Id
    private String id;

    private String cvc;
    private String cardNumber;
    private String nameOnCard;
    private String typeCard;
    private String expiryDate;

    public Card() {
        this.id = String.valueOf(idCounter.incrementAndGet());
    }


}
