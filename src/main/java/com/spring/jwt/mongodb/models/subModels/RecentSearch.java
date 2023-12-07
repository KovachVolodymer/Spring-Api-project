package com.spring.jwt.mongodb.models.subModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
public class RecentSearch {

    @Id
    private Integer id;

    private String city;

    private Integer places;


}
