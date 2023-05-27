package com.owl.api.example.controller;

import com.owl.api.example.dto.CPUResponseDTO;
import com.owl.api.example.dto.RAMResponseDTO;
import com.owl.api.example.service.RAMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ram")
public class RAMController {

    @GetMapping
    public ResponseEntity<List<RAMResponseDTO>> getAllRAMs() {
        return new ResponseEntity<>(this.ramService.getAllRAMs(), HttpStatus.OK);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllCpuNames() {
        List<RAMResponseDTO> rams = this.ramService.getAllRAMs();
        List<String> names = new ArrayList<>();
        for(RAMResponseDTO ram : rams) {
            names.add(ram.getName());
        }
        return new ResponseEntity<>(names, HttpStatus.OK);
    }

    private final RAMService ramService;

    @Autowired
    public RAMController(RAMService ramService) {
        this.ramService = ramService;
    }

}
