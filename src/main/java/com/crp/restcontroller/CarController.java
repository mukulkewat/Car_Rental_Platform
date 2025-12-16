package com.crp.restcontroller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crp.responsedto.CarResponseDTO;
import com.crp.service.CarService;

@RestController
@RequestMapping("/api/cars")
public class CarController {
	@Autowired
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    // 1️ List all cars (public / user-facing)
    @GetMapping
    public ResponseEntity<List<CarResponseDTO>> listAllCars() {
        return ResponseEntity.ok(carService.listAllCars());
    }

    // 2️ Find car by ID
    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDTO> findCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.findCarById(id));
    }

    // 3️ Find available cars between dates
    @GetMapping("/available")
    public ResponseEntity<List<CarResponseDTO>> findAvailableCars(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {

        return ResponseEntity.ok(carService.findAvailableCars(start, end));
    }
}
