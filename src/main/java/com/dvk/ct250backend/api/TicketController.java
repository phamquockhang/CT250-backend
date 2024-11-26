package com.dvk.ct250backend.api;

import com.dvk.ct250backend.app.dto.response.ApiResponse;
import com.dvk.ct250backend.app.dto.response.Page;
import com.dvk.ct250backend.app.exception.ResourceNotFoundException;
import com.dvk.ct250backend.domain.booking.dto.TicketDTO;
import com.dvk.ct250backend.domain.booking.service.TicketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketController {
    TicketService ticketService;


    @PutMapping("/{id}")
    public ApiResponse<Void> updateTicket(@PathVariable("id") Integer ticketId, @RequestBody TicketDTO ticketDTO) throws ResourceNotFoundException {
        ticketService.updateTicket(ticketId, ticketDTO);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTicket(@PathVariable("id") Integer ticketId) throws ResourceNotFoundException {
        ticketService.deleteTicket(ticketId);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @GetMapping
    public ApiResponse<Page<TicketDTO>> getTickets(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<TicketDTO>>builder()
                .status(HttpStatus.OK.value())
                .payload(ticketService.getTickets(params))
                .build();
    }

    @GetMapping("/last-30-days-count")
    public ApiResponse<Integer> getLast30DaysTicketCount() {
        return ApiResponse.<Integer>builder()
                .status(HttpStatus.OK.value())
                .payload(ticketService.getLast30DaysTicketCount())
                .build();
    }
}
