package com.crp.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crp.exception.BusinessException;
import com.crp.exception.ResourceNotFoundException;
import com.crp.mapper.BookingMapper;
import com.crp.model.Booking;
import com.crp.model.BookingStatus;
import com.crp.model.Car;
import com.crp.model.User;
import com.crp.repository.BookingRepo;
import com.crp.repository.CarRepo;
import com.crp.repository.UserRepo;
import com.crp.responsedto.BookingResponseDTO;

@Service
public class BookingService {

    @Autowired private final BookingRepo bookingRepo;
    @Autowired private final CarRepo carRepo;
    @Autowired private final UserRepo userRepo;
    @Autowired private  BookingMapper bookingMapper;

    //  Constructor injection
    public BookingService(
            BookingRepo bookingRepo,
            CarRepo carRepo,
            UserRepo userRepo, BookingMapper bookingMapper) {

        this.bookingRepo = bookingRepo;
        this.carRepo = carRepo;
        this.userRepo = userRepo;
        this.bookingMapper = bookingMapper;
    }

    // 1️ Create booking
    public BookingResponseDTO createBooking(
            Long userId,
            Long carId,
            LocalDate start,
            LocalDate end) {

        if (start == null || end == null) {
            throw new BusinessException("Start date and end date must be provided");
        }

        if (end.isBefore(start)) {
            throw new BusinessException("End date cannot be before start date");
        }

        long days = ChronoUnit.DAYS.between(start, end);
        if (days <= 0) {
            throw new BusinessException("Booking duration must be at least 1 day");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId));

        Car car = carRepo.findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Car not found with id: " + carId));

        List<Car> availableCars = carRepo.findAvailableCars(start, end);
        if (!availableCars.contains(car)) {
            throw new BusinessException(
                    "Car is not available for the selected dates");
        }

        double totalCost = days * car.getDailyRentalRate();

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(car);
        booking.setStartDate(start);
        booking.setEndDate(end);
        booking.setTotalCost(totalCost);
        booking.setStatus(BookingStatus.CONFIRMED);
        BookingResponseDTO mapToBookingResponseDTO = bookingMapper.mapToBookingResponseDTO(booking);
        
        car.setAvailable(false);
        carRepo.save(car);
        bookingRepo.save(booking);
        return mapToBookingResponseDTO;
    }

    // 2️ Get bookings for a user
    public List<BookingResponseDTO> getUserBookings(String email) {

        if (email == null || email.trim().isEmpty()) {
            throw new BusinessException("User email must be provided");
        }
        List<BookingResponseDTO> listOfBooking = bookingRepo.findByUserEmail(email).stream().map(bookingMapper::mapToBookingResponseDTO).toList();

        return listOfBooking;
    }

    // 3️ Get all bookings (ADMIN)
    public List<BookingResponseDTO> getAllBookings() {
    	List<BookingResponseDTO> listOfAllBooking = bookingRepo.findAll().stream().map(bookingMapper::mapToBookingResponseDTO).toList();

        if (listOfAllBooking == null || listOfAllBooking.size()<=0) {
            throw new BusinessException("No Bookings Founds");
        }
        return listOfAllBooking;
    }

    // 4️ Cancel booking (USER → own booking)
    public BookingResponseDTO cancelBooking(Long bookingId, String email) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + bookingId));

        if (!booking.getUser().getEmail().equals(email)) {
            throw new BusinessException(
                    "You can cancel only your own booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        Car car = booking.getCar();
        car.setAvailable(true);
        carRepo.save(car);
        bookingRepo.save(booking);
        BookingResponseDTO bookingResponseDTO = bookingMapper.mapToBookingResponseDTO(booking);
        return bookingResponseDTO;
    }

    // 5️ Complete booking (ADMIN)
    public BookingResponseDTO completeBooking(Long bookingId) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + bookingId));

        booking.setStatus(BookingStatus.COMPLETED);

        Car car = booking.getCar();
        car.setAvailable(true);
        carRepo.save(car);
        BookingResponseDTO bookingResponseDTO = bookingMapper.mapToBookingResponseDTO(booking);
        bookingRepo.save(booking);
        return bookingResponseDTO;
    }
}
