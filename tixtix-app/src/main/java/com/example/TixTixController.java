package com.example;

import com.example.dto.TicketingRequest;
import com.example.dto.TicketingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


}
