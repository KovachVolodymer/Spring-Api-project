package com.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
public class Reviews {
   private String photo;
   private String name;
   private String rating;
}
