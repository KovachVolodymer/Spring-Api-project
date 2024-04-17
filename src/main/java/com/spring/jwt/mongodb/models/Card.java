package com.spring.jwt.mongodb.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
@Setter
@Getter
public class Card {
    @Id
    private String id;

    private String cvc;
    private String cardNumber;
    private String nameOnCard;
    private String typeCard;
    private String expiryDate;


}
