package com.spring.jwt.mongodb.models;

import java.util.*;

import com.spring.jwt.mongodb.models.subModels.RecentSearch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
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

  private String birthday;

  private Set<Hotel> favoritesHotels = new HashSet<>();

  private Set<Flight> favoritesFlights = new HashSet<>();

  private List<RecentSearch> recentSearch=new ArrayList<>();


  public User(String username, String email, String password, String phone) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.phone = phone;
  }

  public User() {

  }
}
