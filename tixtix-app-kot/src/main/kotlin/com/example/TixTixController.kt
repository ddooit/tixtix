package com.example

import com.example.dto.TicketingRequestDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tix")
class TixTixController(
    val service: TixTixService
) {

    @GetMapping("/ping")
    fun ping(): String = "pong 퐁퐁"

    @PostMapping()
    fun ticketing(@RequestBody requestDto: TicketingRequestDto): String = service.ticketing(requestDto)

}