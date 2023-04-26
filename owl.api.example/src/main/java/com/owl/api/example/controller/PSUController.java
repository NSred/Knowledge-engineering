package com.owl.api.example.controller;

import com.owl.api.example.dto.PSUResponseDTO;
import com.owl.api.example.service.PSUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/psu")
public class PSUController {

    @GetMapping
    public ResponseEntity<List<PSUResponseDTO>> getAllPSUs() {
        return new ResponseEntity<>(this.psuService.getAllPSUs(), HttpStatus.OK);
    }

    private final PSUService psuService;

    @Autowired
    public PSUController(PSUService psuService) {
        this.psuService = psuService;
    }

}
