package com.spring.jwt.mongodb.payload.request;

import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    @NotBlank(message = "name is required")
    @Size(min = 3,message = "First name must be at least 3 characters")
    @Size(max = 20,message = "First name must be less than 20 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    @Size(max = 50)
    private String email;

    private Set<String> roles;

    @NotBlank
    @Size(min = 6,message = "Password must be at least 6 characters")
    @Size(max = 40,message = "Password must be less than 40 characters")
    private String password;

    @NotBlank
    @Size(min = 10,message = "Phone number must be at least 10 characters")
    private String phone;


}
