package com.owl.api.example.controller;

import com.owl.api.example.dto.CauseProbabilityDTO;
import com.owl.api.example.dto.PurposeTypeDTO;
import com.owl.api.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/component")
public class ComponentController {

    @GetMapping
    public ResponseEntity<?> getComponent(
            @RequestParam(required = true) String component,
            @RequestParam(required = true) String motherboard,
            @RequestParam(required = true) String cpu,
            @RequestParam(required = true) String gpu,
            @RequestParam(required = true) String ram,
            @RequestParam(required = true) String psu) {
        if (component.equals("CPU"))
            return new ResponseEntity<>(this.cpuService.getCPUUpgrades(cpu, motherboard, gpu), HttpStatus.OK);
        else if (component.equals("GPU"))
            return new ResponseEntity<>(this.gpuService.getGPUUpgrades(gpu, motherboard, cpu, psu), HttpStatus.OK);
        else if (component.equals("PSU"))
            return new ResponseEntity<>(this.psuService.getPSUUpgrades(psu, motherboard, gpu), HttpStatus.OK);
        else if (component.equals("RAM"))
            return new ResponseEntity<>(this.ramService.getRAMUpgrades(ram, motherboard), HttpStatus.OK);
        else if (component.equals("Motherboard"))
            return new ResponseEntity<>(this.motherboardService.getMotherboardUpgrades(motherboard, cpu, gpu, ram, psu), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/purpose")
    public ResponseEntity<PurposeTypeDTO> getPurposeType(
            @RequestParam(required = true) double cpu_clock_speed_ghz,
            @RequestParam(required = true) int ram_capacity_gb,
            @RequestParam(required = true) int cpu_cores,
            @RequestParam(required = true) int cpu_threads,
            @RequestParam(required = true) double gpu_video_memory_gb,
            @RequestParam(required = true) int gpu_core_clock_mhz,
            @RequestParam(required = true) double hard_drive_capacity_gb,
            @RequestParam(required = true) int psu_power_watts,
            @RequestParam(required = true) int l3_size_mb,
            @RequestParam(required = true) int ram_latency_ns) {
        return new ResponseEntity<>(this.fuzzyLogicService.getPurposeType(cpu_clock_speed_ghz, ram_capacity_gb, cpu_cores, cpu_threads, gpu_video_memory_gb,
                gpu_core_clock_mhz, hard_drive_capacity_gb, psu_power_watts, l3_size_mb, ram_latency_ns), HttpStatus.OK);
    }

    @GetMapping("/cause")
    public ResponseEntity<?> getProbabilityOfCause(
            @RequestParam(required = true) String allCauses) {
        List<String> causes = Arrays.asList(allCauses.split(","));
        try{
            return new ResponseEntity<>(this.bayesService.getAllProbabilities(causes), HttpStatus.OK);
        }
        catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private final RAMService ramService;
    private final PSUService psuService;
    private final CPUService cpuService;
    private final GPUService gpuService;
    private final MotherboardService motherboardService;
    private final FuzzyLogicService fuzzyLogicService;
    private final BayesService bayesService;

    @Autowired
    public ComponentController(RAMService ramService, PSUService psuService, CPUService cpuService, GPUService gpuService, MotherboardService motherboardService, FuzzyLogicService fuzzyLogicService, BayesService bayesService) {
        this.ramService = ramService;
        this.psuService = psuService;
        this.cpuService = cpuService;
        this.gpuService = gpuService;
        this.motherboardService = motherboardService;
        this.fuzzyLogicService = fuzzyLogicService;
        this.bayesService = bayesService;
    }

}
