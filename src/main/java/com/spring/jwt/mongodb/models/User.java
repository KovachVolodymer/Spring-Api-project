package com.spring.jwt.mongodb.models;

import java.util.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Getter
@Setter
@Document(collection = "User")
public class User {
  @Id
  private String id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 120)
  private String password;

  @DBRef
  private Set<Role> roles = new HashSet<>();

  private String avatar;

  private String phone;

  private String address;

  private String dataBirth;

  private String newEmail;

  private List<Favorites> favoritesList = new ArrayList<>();

  private List<RecentSearch> recentSearches=new ArrayList<>();


  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }



}
