package com.owl.api.example.controller;

import com.owl.api.example.dto.CPURequestDTO;
import com.owl.api.example.dto.CPUResponseDTO;
import com.owl.api.example.service.CPUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cpu")
public class CPUController {

    @GetMapping
    public ResponseEntity<List<CPUResponseDTO>> getAllCPUs() {
        return new ResponseEntity<>(this.CPUService.getAllCPUs(), HttpStatus.OK);
    }

    @GetMapping("/cpu-by-spec")
    public ResponseEntity<List<CPUResponseDTO>> getCPUBySpec(
            @RequestParam(required = false, defaultValue = "0") int lowerTDP,
            @RequestParam(required = false, defaultValue = "1000") int higherTDP,
            @RequestParam(required = false, defaultValue = "0") double lowerClockSpeed,
            @RequestParam(required = false, defaultValue = "1000") double higherClockSpeed,
            @RequestParam(required = false, defaultValue = "0") int lowerCores,
            @RequestParam(required = false, defaultValue = "1000") int higherCores,
            @RequestParam(required = false, defaultValue = "0") int lowerThreads,
            @RequestParam(required = false, defaultValue = "1000") int higherThreads) {
        if(lowerCores > higherCores || lowerClockSpeed > higherClockSpeed || lowerTDP > higherTDP || lowerThreads > higherThreads
            || lowerCores < 0 || higherCores < 0 || lowerClockSpeed < 0 || higherClockSpeed < 0 || lowerTDP < 0 || higherTDP < 0
            || lowerThreads < 0 || higherThreads < 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        CPURequestDTO cpu = new CPURequestDTO();
        cpu.setHigherTDP(higherTDP);
        cpu.setLowerTDP(lowerTDP);
        cpu.setHigherCores(higherCores);
        cpu.setLowerCores(lowerCores);
        cpu.setHigherThreads(higherThreads);
        cpu.setLowerThreads(lowerThreads);
        cpu.setLowerClockSpeed(lowerClockSpeed);
        cpu.setHigherClockSpeed(higherClockSpeed);
        return new ResponseEntity<>(this.CPUService.getCPUBySpec(cpu), HttpStatus.OK);
    }

    private final CPUService CPUService;

    @Autowired
    public CPUController(CPUService CPUService) {
        this.CPUService = CPUService;
    }

}
