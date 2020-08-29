package com.infinum.cities.api;

import com.infinum.cities.model.City;
import com.infinum.cities.service.CityService;
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<City> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<City>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/oldest-first")
    public ResponseEntity<Iterable<City>> findAllOrderByCreated() {
        return ResponseEntity.ok(service.findAllOrderByCreated());
    }

    @GetMapping("/most-favorites")
    public ResponseEntity<Iterable<City>> findAllOrderByMostFavorites() {
        return ResponseEntity.ok(service.findAllOrderByMostFavorites());
    }

    @PostMapping
    public ResponseEntity<City> save(@Valid @RequestBody City city) {
        return ResponseEntity.ok(service.save(city));
    }
}
