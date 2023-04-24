package com.owl.api.example.dto;

import lombok.Data;

@Data
public class GPUResponseDTO {
    private String name;
    private int TDP;
    private int coreClock;
    private int memoryClock;
    private int videoMemory;
}

