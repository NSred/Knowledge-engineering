package com.owl.api.example.controller;

import com.owl.api.example.dto.CPUResponseDTO;
import com.owl.api.example.dto.MotherboardResponseDTO;
import com.owl.api.example.service.MotherboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/motherboard")
public class MotherboardController {

    @GetMapping
    public ResponseEntity<List<MotherboardResponseDTO>> getAllMotherboards() {
        return new ResponseEntity<>(this.motherboardService.getAllMotherboards(), HttpStatus.OK);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllMotherboardNames() {
        List<MotherboardResponseDTO> motherboards = this.motherboardService.getAllMotherboards();
        List<String> names = new ArrayList<>();
        for(MotherboardResponseDTO m : motherboards) {
            names.add(m.getName());
        }
        return new ResponseEntity<>(names, HttpStatus.OK);
    }

    private final MotherboardService motherboardService;

    @Autowired
    public MotherboardController(MotherboardService motherboardService) {
        this.motherboardService = motherboardService;
    }

}
