package com.crp.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crp.requestdto.CarRequestDTO;
import com.crp.responsedto.CarResponseDTO;
import com.crp.service.CarService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/cars")
@PreAuthorize("hasRole('ADMIN')") // ADMIN only
public class AdminCarController {
	
    @Autowired private  CarService carService;

    public AdminCarController(CarService carService) {
        this.carService = carService;
    }

    // 1️ ADMIN → Add new car
    @PostMapping
    public ResponseEntity<CarResponseDTO> addCar(@Valid @RequestBody CarRequestDTO car) {
        return ResponseEntity.ok(carService.addCar(car));
    }

    // 2️ ADMIN → Delete car
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.ok("Car deleted successfully");
    }

    // 3️ ADMIN → Update availability
    @PutMapping("/{id}/availability")
    public ResponseEntity<CarResponseDTO> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {

        return ResponseEntity.ok(carService.updateAvailability(id, available));
    }
}

