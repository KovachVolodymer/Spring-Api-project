package com.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
public class Reviews {
    @Id
    private String id;
    private String photo;
    private String name;
    private String rating;
}
