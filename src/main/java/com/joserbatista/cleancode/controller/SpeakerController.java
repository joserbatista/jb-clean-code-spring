package com.joserbatista.cleancode.controller;

import com.joserbatista.cleancode.domain.Speaker;
import com.joserbatista.cleancode.service.SpeakerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/speaker")
public class SpeakerController {

    @Autowired
    SpeakerService service;

    @Operation(summary = "Register new speaker")
    @ApiResponse(
        responseCode = "201",
        description = "Default speakers created"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Speaker  not found"
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error"
    )
    @Validated
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Speaker> register(@RequestBody @Valid Speaker speaker)
        throws SpeakerService.NoSessionsApprovedException, SpeakerService.ArgumentNullException, SpeakerService.SpeakerDoesntMeetRequirementsException {
        return ResponseEntity.ok(this.service.register(speaker));
    }

    @ApiResponse(
        responseCode = "200",
        description = "The speaker for the ID"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Speaker  not found"
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error"
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Speaker> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
}