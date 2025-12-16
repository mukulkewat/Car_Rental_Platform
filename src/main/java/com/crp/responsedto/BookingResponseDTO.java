package com.crp.responsedto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
	
	private Long bookingId;
    private Long carId;
    private String carName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalCost;
    private String status;

    private Long userId;
    private String userEmail;
}

