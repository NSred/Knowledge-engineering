package com.owl.api.example.controller;

import com.owl.api.example.dto.GPURequestDTO;
import com.owl.api.example.dto.GPUResponseDTO;
import com.owl.api.example.service.GPUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gpu")
public class GPUController {

    @GetMapping
    public ResponseEntity<List<GPUResponseDTO>> getAllGPUs() {
        return new ResponseEntity<>(this.GPUService.getAllGPUs(), HttpStatus.OK);
    }

    @GetMapping("/gpu-by-spec")
    public ResponseEntity<List<GPUResponseDTO>> getCPUBySpec(
            @RequestParam(required = false, defaultValue = "0") int lowerTDP,
            @RequestParam(required = false, defaultValue = "100000") int higherTDP,
            @RequestParam(required = false, defaultValue = "0") int lowerVideoMemory,
            @RequestParam(required = false, defaultValue = "100000") int higherVideoMemory,
            @RequestParam(required = false, defaultValue = "0") int lowerCoreClock,
            @RequestParam(required = false, defaultValue = "100000") int higherCoreClock,
            @RequestParam(required = false, defaultValue = "0") int lowerMemoryClock,
            @RequestParam(required = false, defaultValue = "100000") int higherMemoryClock) {
        if(lowerCoreClock > higherCoreClock || lowerMemoryClock > higherMemoryClock || lowerTDP > higherTDP || lowerVideoMemory > higherVideoMemory
            || lowerCoreClock < 0 || higherCoreClock < 0 || lowerMemoryClock < 0 || higherMemoryClock < 0 || lowerTDP < 0 || higherTDP < 0
            || lowerVideoMemory < 0 || higherVideoMemory < 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        GPURequestDTO gpu = new GPURequestDTO();
        gpu.setHigherTDP(higherTDP);
        gpu.setLowerTDP(lowerTDP);
        gpu.setHigherCoreClock(higherCoreClock);
        gpu.setLowerCoreClock(lowerCoreClock);
        gpu.setHigherMemoryClock(higherMemoryClock);
        gpu.setLowerMemoryClock(lowerMemoryClock);
        gpu.setHigherVideoMemory(higherVideoMemory);
        gpu.setLowerVideoMemory(lowerVideoMemory);
        return new ResponseEntity<>(this.GPUService.getGPUBySpec(gpu), HttpStatus.OK);
    }

    private final GPUService GPUService;

    @Autowired
    public GPUController(GPUService GPUService) {
        this.GPUService = GPUService;
    }

}
