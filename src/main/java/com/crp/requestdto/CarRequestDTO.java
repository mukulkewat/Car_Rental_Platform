package com.crp.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestDTO {

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @Min(value = 1990, message = "Year must be valid")
    private int year;

    @NotBlank(message = "License plate is required")
    private String licensePlate;

    @Min(value = 1, message = "Daily rental rate must be positive")
    private double dailyRentalRate;

    // Admin-controlled flag
    private boolean available;
}
