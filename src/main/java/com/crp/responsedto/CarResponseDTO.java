package com.crp.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarResponseDTO {

    private Long id;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private double dailyRentalRate;
    private boolean available;
}
