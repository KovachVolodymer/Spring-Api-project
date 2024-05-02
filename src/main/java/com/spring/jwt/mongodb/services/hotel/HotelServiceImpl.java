package com.spring.jwt.mongodb.services.hotel;

import com.spring.jwt.mongodb.models.Hotel;
import com.spring.jwt.mongodb.models.Reviews;
import com.spring.jwt.mongodb.models.Room;
import com.spring.jwt.mongodb.payload.response.MessageResponse;
import com.spring.jwt.mongodb.repository.HotelsRepository;
import com.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@Service
public class HotelServiceImpl implements HotelService{

    @Autowired
    HotelsRepository hotelsRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseEntity<List<Map<String, Object>>> hotels() {
        List<Hotel> hotelsList = hotelsRepository.findAll();

        List<Map<String, Object>> hotelMaps = hotelsList.stream()
                .map(hotel -> {
                    Map<String, Object> hotelMap = new HashMap<>();
                    hotelMap.put("id", hotel.getId());
                    hotelMap.put("photo", hotel.getPhoto());
                    hotelMap.put("name", hotel.getName());
                    hotelMap.put("rating", hotel.getRating());
                    hotelMap.put("price", hotel.getPrice());
                    hotelMap.put("location", hotel.getLocation());
                    hotelMap.put("advantages", hotel.getAdvantages());
                    hotelMap.put("slug", hotel.getSlug());
                    return hotelMap;
                })
                .collect(Collectors.toList());

        return ok(hotelMaps);
    }

    @Override
    public ResponseEntity<Hotel> hotelById(String id) {
        Optional<Hotel> hotelData = hotelsRepository.findById(id);
        return hotelData.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @Override
    public ResponseEntity<Hotel> addHotel(Hotel hotel) {
        if (hotel.getId() != null && hotelsRepository.existsById(hotel.getId())) {
            // If a hotel with the specified id already exists, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            // If no id is specified or there is no existing hotel with the given id, save the new hotel
            String slug = hotel.getName().toLowerCase()
                    .replaceAll(" ", "_")
                    .replaceAll("[^a-z0-9_-]", "");
            hotel.setSlug(slug);
            Hotel newHotel = hotelsRepository.save(hotel);
            return ResponseEntity.status(HttpStatus.CREATED).body(newHotel);
        }
    }

    @Override
    public ResponseEntity<Hotel> updateHotel(String id, Hotel hotel) {
        Optional<Hotel> existingHotel = hotelsRepository.findById(id);
        if (existingHotel.isPresent()) {
            Hotel hotelData = existingHotel.get();
            hotelData.setName(hotel.getName());
            hotelData.setPrice(hotel.getPrice());
            hotelData.setLocation(hotel.getLocation());
            hotelData.setRating(hotel.getRating());
            hotelData.setDescription(hotel.getDescription());
            hotelData.setPhoto(hotel.getPhoto());
            hotelData.setAdvantages(hotel.getAdvantages());
            hotelData.setReviews(hotel.getReviews());
            Hotel updatedHotel = hotelsRepository.save(hotelData);
            return ResponseEntity.ok(updatedHotel);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @Override
    public ResponseEntity<Object> deleteHotel(String id) {
        Optional<Hotel> hotelData = hotelsRepository.findById(id);
        if (hotelData.isPresent()) {
            try {
                hotelsRepository.deleteById(id);
                return ResponseEntity.ok(new MessageResponse("Hotel deleted successfully"));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Hotel not found"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Hotel not found"));
        }
    }

    @Override
    public ResponseEntity<Hotel> patchHotel(String id, Hotel hotel) {
        Optional<Hotel> optionalHotel = hotelsRepository.findById(id);
        optionalHotel.ifPresent(h -> {
            Optional.ofNullable(hotel.getName()).ifPresent(h::setName);
            Optional.ofNullable(hotel.getLocation()).ifPresent(h::setLocation);
            Optional.ofNullable(hotel.getPrice()).ifPresent(h::setPrice);
            Optional.ofNullable(hotel.getPhoto()).ifPresent(h::setPhoto);
            Optional.ofNullable(hotel.getRating()).ifPresent(h::setRating);
            Optional.ofNullable(hotel.getAdvantages()).ifPresent(h::setAdvantages);
            Optional.ofNullable(hotel.getReviews()).ifPresent(h::setReviews);
            Optional.ofNullable(hotel.getDescription()).ifPresent(h::setDescription);
            Optional.ofNullable(hotel.getAlt()).ifPresent(h::setAlt);
            hotelsRepository.save(h);
        });
        return optionalHotel.map(ResponseEntity::ok).orElseGet(() -> notFound().build());
    }

    @Override
    public ResponseEntity<Object> getUniqueAdvantages() {
        List<Hotel> hotelsList = hotelsRepository.findAll();
        List<String> advantagesList = new ArrayList<>();
        for (Hotel hotel : hotelsList) {
            for (String advantage : hotel.getAdvantages()) {
                if (!advantagesList.contains(advantage)) {
                    advantagesList.add(advantage);


                }
            }
        }
        return ok(advantagesList
                .stream()
                .sorted()
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<Object> addReview(String id, Reviews review) {
        Optional<Hotel> hotelData = hotelsRepository.findById(id);
        if (hotelData.isPresent()) {
            Hotel hotel = hotelData.get();
            hotel.getReviews().add(review);
            hotelsRepository.save(hotel);
            return ResponseEntity.ok(new MessageResponse("Review added successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Hotel not found"));
        }
    }

    @Override
    public ResponseEntity<Object> filterByPrice(String maxPrice, String minPrice, String rating, List<String> advantages, String sort) {
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

    @Override
    public ResponseEntity<Object> addRoom(Room room, String id) {
        hotelsRepository.findById(id).ifPresent(hotel -> {
            hotel.getRooms().add(room);
            hotelsRepository.save(hotel);
        });
        return ResponseEntity.ok().body(new MessageResponse("Room added"));
    }

    @Override
    public ResponseEntity<Object> deleteRoom(String id, String roomId) {



        hotelsRepository.findById(id).ifPresent(hotel -> {
            hotel.getRooms().removeIf(room -> room.getId().equals(roomId));
            hotelsRepository.save(hotel);
        });
        return ResponseEntity.ok().body(new MessageResponse("Room deleted"));
    }
}
