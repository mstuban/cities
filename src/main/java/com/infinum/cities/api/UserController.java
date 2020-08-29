package com.infinum.cities.api;

import com.infinum.cities.model.User;
import com.infinum.cities.model.dto.PatchOperation;
import com.infinum.cities.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PatchMapping("/favorite-cities/{op}/{city-name}")
    public ResponseEntity<User> modifyFavoriteCities(@PathVariable PatchOperation op, @PathVariable("city-name") String cityName) {
        return ResponseEntity.ok(service.modifyFavoriteCities(op, cityName, "mstuban@gmail.com"));
    }
}
