package com.spring.jwt.mongodb.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
