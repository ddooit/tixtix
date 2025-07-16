package com.example;

import com.example.dto.BulkTicketingRequest;
import com.example.dto.BulkTicketingResponse;
import com.example.dto.TicketingRequest;
import com.example.dto.TicketingResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class TixTixController {
    private final TixTixService service;

    TixTixController(final TixTixService service) {
        this.service = service;
    }

    @GetMapping("/ping")
    String ping() {
        return "pong";
    }

    @PostMapping("/ticketing")
    TicketingResponse ticketing(@RequestBody final TicketingRequest request) {
        return service.ticketing(request);
    }

    @GetMapping("/{performanceId}")
    List<Long> getPerformance(@PathVariable final long performanceId) {
        return service.monitoring(performanceId);
    }

    @PostMapping("/bulk-ticketing")
    BulkTicketingResponse bulkTicketing(@RequestBody final List<BulkTicketingRequest> requests) {
        return service.bulkTicketing(requests);
    }

}
