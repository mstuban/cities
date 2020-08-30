package com.infinum.cities.api;

import com.infinum.cities.model.City;
import com.infinum.cities.service.CityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/city")
public class CityController {

    private final CityService service;

    public CityController(CityService service) {
        this.service = service;
    }

    @ApiOperation(value = "Retrieve all cities")
    @GetMapping
    public ResponseEntity<Iterable<City>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @ApiOperation(value = "Retrieve cities sorted by created date (oldest first)")
    @GetMapping("/oldest-first")
    public ResponseEntity<Iterable<City>> findAllOrderByCreated() {
        return ResponseEntity.ok(service.findAllOrderByCreated());
    }

    @ApiOperation(value = "Retrieve cities sorted by times favorited by users (least favorited first)")
    @GetMapping("/most-favorites")
    public ResponseEntity<Iterable<City>> findAllOrderByMostFavorites() {
        return ResponseEntity.ok(service.findAllOrderByMostFavorites());
    }

    @ApiOperation(value = "Save new city")
    @PostMapping("/save")
    public ResponseEntity<City> save(
        @ApiParam(value = "City object", required = true) @Valid @RequestBody City city
    ) {
        return ResponseEntity.ok(service.save(city));
    }
}
