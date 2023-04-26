package com.owl.api.example.controller;

import com.owl.api.example.dto.RAMResponseDTO;
import com.owl.api.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/component")
public class ComponentController {

    @GetMapping
    public ResponseEntity<String> getComponent(@RequestParam String component) {
        if (component.equals("CPU"))
            return new ResponseEntity<>("CPU", HttpStatus.OK);
        else if (component.equals("GPU"))
            return new ResponseEntity<>("GPU", HttpStatus.OK);
        else if (component.equals("PSU"))
            return new ResponseEntity<>("PSU", HttpStatus.OK);
        else if (component.equals("RAM"))
            return new ResponseEntity<>("RAM", HttpStatus.OK);
        else if (component.equals("Motherboard"))
            return new ResponseEntity<>("Motherboard", HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private final RAMService ramService;
    private final PSUService psuService;
    private final CPUService cpuService;
    private final GPUService gpuService;
    private final MotherboardService motherboardService;

    @Autowired
    public ComponentController(RAMService ramService, PSUService psuService, CPUService cpuService, GPUService gpuService, MotherboardService motherboardService) {
        this.ramService = ramService;
        this.psuService = psuService;
        this.cpuService = cpuService;
        this.gpuService = gpuService;
        this.motherboardService = motherboardService;
    }

}
