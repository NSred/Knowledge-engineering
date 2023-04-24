package com.owl.api.example.dto;

import lombok.Data;

@Data
public class GPURequestDTO {
    private int lowerTDP;
    private int higherTDP;
    private int lowerVideoMemory;
    private int higherVideoMemory;
    private int lowerCoreClock;
    private int higherCoreClock;
    private int lowerMemoryClock;
    private int higherMemoryClock;
}

