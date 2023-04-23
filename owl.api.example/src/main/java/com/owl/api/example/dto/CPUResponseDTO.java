package com.owl.api.example.dto;

import lombok.Data;

@Data
public class CPUResponseDTO {
    private String name;
    private int TDP;
    private double clockSpeed;
    private int cores;
    private int threads;
}

