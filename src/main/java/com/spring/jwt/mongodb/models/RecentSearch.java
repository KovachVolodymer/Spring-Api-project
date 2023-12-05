package com.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
public class RecentSearch {

    private String userId;

    @Id
    private Integer id;

    private String city;

    private Integer places;


}
