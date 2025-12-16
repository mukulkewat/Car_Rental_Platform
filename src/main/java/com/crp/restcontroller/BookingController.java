package com.crp.restcontroller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.crp.responsedto.BookingResponseDTO;
import com.crp.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
	@Autowired
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // 1️ USER → Create booking
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @RequestParam Long userId,
            @RequestParam Long carId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(
                bookingService.createBooking(userId, carId, startDate, endDate)
        );
    }

    // 2️ USER → Get own bookings
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // email from JWT

        return ResponseEntity.ok(bookingService.getUserBookings(email));
    }

    // 3️ ADMIN → Get all bookings
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // 4️ USER → Cancel OWN booking
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cancel/{id}")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        return ResponseEntity.ok(
                bookingService.cancelBooking(id, email)
        );
    }

    // 5️ ADMIN → Complete booking
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/complete/{id}")
    public ResponseEntity<BookingResponseDTO> completeBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }
}
