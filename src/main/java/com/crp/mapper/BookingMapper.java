package com.crp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import com.crp.model.Booking;
import com.crp.responsedto.BookingResponseDTO;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Named("mapToBookingResponseDTO")
    default BookingResponseDTO mapToBookingResponseDTO(Booking booking) {
    	
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setBookingId(booking.getId());
        dto.setCarId(booking.getCar().getId());
        dto.setCarName(
                booking.getCar().getMake() + " " + booking.getCar().getModel());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        dto.setTotalCost(booking.getTotalCost());
        dto.setStatus(booking.getStatus().name());
        dto.setUserId(booking.getUser().getId());
        dto.setUserEmail(booking.getUser().getEmail());

        return dto;
    }

}
