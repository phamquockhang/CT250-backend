package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.service.BookingService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingService bookingService;

    @PostMapping
    public ApiResponse<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        return ApiResponse.<BookingDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(bookingService.createInitBooking(bookingDTO))
                .build();
    }

    @PutMapping("/{bookingId}/reserve")
    public ApiResponse<BookingDTO> reserveBooking(@PathVariable Integer bookingId, @RequestBody BookingDTO bookingDTO) throws ResourceNotFoundException {
        return ApiResponse.<BookingDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(bookingService.reserveBooking(bookingId, bookingDTO))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<BookingDTO>> getBookings(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<BookingDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(bookingService.getBookings(params))
                .build();
    }

    @GetMapping("/search")
    public ApiResponse<String> searchBookings(@RequestParam(name = "code") String code, HttpServletResponse response) throws IOException, ResourceNotFoundException {
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .payload(bookingService.searchBooking(code, response))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<BookingDTO> getBookingById(@PathVariable Integer id) throws ResourceNotFoundException {
        return ApiResponse.<BookingDTO>builder()
                .status(HttpStatus.OK.value())
                .payload(bookingService.getBookingById(id))
                .build();
    }

    @GetMapping("/last-30-days-sales")
    public ApiResponse<BigDecimal> getLast30DaysSales() {
        return ApiResponse.<BigDecimal>builder()
                .status(HttpStatus.OK.value())
                .payload(bookingService.getLast30DaysSales())
                .build();
    }

    @GetMapping("/last-30-days-count")
    public ApiResponse<Integer> getLast30DaysBookingCount() {
        return ApiResponse.<Integer>builder()
                .status(HttpStatus.OK.value())
                .payload(bookingService.getLast30DaysBookingCount())
                .build();
    }

    @GetMapping("/sales-stats")
    public ApiResponse<Map<String, BigDecimal>> getSalesStats(@RequestParam Map<String, String> params) {
        return ApiResponse.<Map<String, BigDecimal>>builder()
                .status(HttpStatus.OK.value())
                .payload(bookingService.getSalesStatistics(params))
                .build();
    }

}



