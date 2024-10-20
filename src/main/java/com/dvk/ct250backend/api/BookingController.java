package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.domain.booking.dto.BookingDTO;
import com.dvk.ct250backend.domain.booking.service.BookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingService bookingService;

    @PostMapping("/create")
    public ApiResponse<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        return ApiResponse.<BookingDTO>builder()
                .status(HttpStatus.CREATED.value())
                .payload(bookingService.createBooking(bookingDTO))
                .build();
    }
}
