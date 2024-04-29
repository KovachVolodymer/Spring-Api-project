package com.spring.jwt.mongodb.controllers.hotel;

import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/hotels")
public class FilterHotelController {

    @Autowired
    HotelsRepository hotelsRepository;

    @GetMapping("/filter")
        public ResponseEntity<Object> filterByPrice(@RequestParam (name = "maxPrice" ,defaultValue = "500") String maxPrice,
                                                    @RequestParam (name = "minPrice", defaultValue = "0") String minPrice,
                                                    @RequestParam (name = "rating", defaultValue = "0") String rating,
                                                    @RequestParam (name = "advantages",defaultValue ="") List<String> advantages,
                                                    @RequestParam (name = "sort",defaultValue ="") String sort) {

        int minPriceInt = Integer.parseInt(minPrice);
        int maxPriceInt = Integer.parseInt(maxPrice);
        double ratingDouble = Double.parseDouble(rating);

        List<Hotel> hotels = advantages.isEmpty()
                ? hotelsRepository.filter(minPriceInt, maxPriceInt, ratingDouble)
                : hotelsRepository.filter(minPriceInt, maxPriceInt, ratingDouble, advantages);

//        switch (sort) {
//            case "maxPrice":
//                hotels.sort((Comparator.comparing(Hotel::getPrice)).reversed());
//                break;
//            case "minPrice":
//                hotels.sort(Comparator.comparing(Hotel::getPrice));
//                break;
//            case "rating":
//                hotels.sort((h1, h2) -> h2.getRating().compareTo(h1.getRating()));
//                break;
//            case "advantages":
//                hotels.sort((h1, h2) -> h2.getAdvantages().size() - h1.getAdvantages().size());
//                break;
//            default:
//                break;
//        }


        return hotels.isEmpty()
                ? ResponseEntity.badRequest().body(new MessageResponse("No hotels found"))
                : ResponseEntity.ok(hotels);
    }

}
