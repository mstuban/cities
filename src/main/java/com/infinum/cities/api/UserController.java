package com.infinum.cities.api;

import com.infinum.cities.model.User;
import com.infinum.cities.model.enums.PatchOperation;
import com.infinum.cities.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "Add or remove favorite cities for a user")
    @PatchMapping("/favorite-cities/{op}/{city-name}")
    public ResponseEntity<User> modifyFavoriteCities(
        @PathVariable PatchOperation op,
        @PathVariable("city-name") String cityName,
        Principal principal
    ) {
        return ResponseEntity.ok(
            service.modifyFavoriteCities(
                op,
                cityName,
                principal.getName()
            ));
    }
}
