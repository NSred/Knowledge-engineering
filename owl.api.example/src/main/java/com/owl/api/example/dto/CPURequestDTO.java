package com.owl.api.example.dto;

import lombok.Data;

@Data
public class CPURequestDTO {
    private int lowerTDP;
    private int higherTDP;
    private double lowerClockSpeed;
    private double higherClockSpeed;
    private int lowerCores;
    private int higherCores;
    private int lowerThreads;
    private int higherThreads;
}

