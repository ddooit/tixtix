package com.example;

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


}
