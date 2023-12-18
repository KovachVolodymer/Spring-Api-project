package com.spring.jwt.mongodb.controllers.subControllers;

import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/hotels")
public class FilterHotelController {

    @Autowired
    HotelsRepository hotelsRepository;

    @GetMapping("/filter")
        public ResponseEntity<Object> filterByPrice(@RequestParam (name = "maxPrice" ,defaultValue = "500") Integer maxPrice,
                                                    @RequestParam (name = "minPrice", defaultValue = "0") Integer minPrice,
                                                    @RequestParam (name = "rating", defaultValue = "0") Double rating,
                                                    @RequestParam (name = "advantages",defaultValue ="") List<String> advantages,
                                                    @RequestParam (name = "sort",defaultValue ="") String sort) {



        List<Hotel> hotels = advantages.isEmpty()
                ? hotelsRepository.filter(minPrice, maxPrice, rating)
                : hotelsRepository.filter(minPrice, maxPrice, rating, advantages);

        switch (sort) {
            case "maxPrice":
                hotels.sort((Comparator.comparing(Hotel::getPrice)).reversed());
                break;
            case "minPrice":
                hotels.sort(Comparator.comparing(Hotel::getPrice));
                break;
            case "rating":
                hotels.sort((h1, h2) -> h2.getRating().compareTo(h1.getRating()));
                break;
            case "advantages":
                hotels.sort((h1, h2) -> h2.getAdvantages().size() - h1.getAdvantages().size());
                break;
            default:
                break;
        }


        return hotels.isEmpty()
                ? ResponseEntity.badRequest().body("No hotels found")
                : ResponseEntity.ok(hotels);
    }

}
