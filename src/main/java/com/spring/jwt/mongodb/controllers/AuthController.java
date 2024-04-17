package com.spring.jwt.mongodb.controllers;

import java.util.*;

import com.spring.jwt.mongodb.payload.response.MessageResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.jwt.mongodb.models.subModels.ERole;
import com.spring.jwt.mongodb.models.Role;
import com.spring.jwt.mongodb.models.User;
import com.spring.jwt.mongodb.payload.request.LoginRequest;
import com.spring.jwt.mongodb.payload.request.SignupRequest;
import com.spring.jwt.mongodb.repository.RoleRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import com.spring.jwt.mongodb.security.jwt.JwtUtils;
import com.spring.jwt.mongodb.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        if (!userRepository.existsByEmail(loginRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is not found!"));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        User user = (User) userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("token", jwt);
        responseMap.put("id", userDetails.getId());
        responseMap.put("username", userDetails.getUsername());
        responseMap.put("email", userDetails.getEmail());
        responseMap.put("avatar", user.getAvatar());
        responseMap.put("favoritesHotel", user.getFavoritesHotels());
        responseMap.put("favoritesFlight", user.getFavoritesFlights());
        responseMap.put("recentSearches", user.getRecentSearch());
        responseMap.put("phone", user.getPhone());
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPhone());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), signUpRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String message = "User registered successfully!";

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", message);
        responseMap.put("token", jwt);
        responseMap.put("id", userDetails.getId());
        responseMap.put("username", userDetails.getUsername());
        responseMap.put("email", userDetails.getEmail());
        responseMap.put("avatar", user.getAvatar());
        responseMap.put("favoritesHotel", user.getFavoritesHotels());
        responseMap.put("favoritesFlight", user.getFavoritesFlights());
        responseMap.put("recentSearches", user.getRecentSearch());
        responseMap.put("phone", user.getPhone());

        return ResponseEntity.ok().body(responseMap);
    }
}
