package com.owl.api.example.dto;

import lombok.Data;

@Data
public class RAMResponseDTO {
    private String name;
    private String type;
    private int latency;
    private double voltage;
    private int capacity;
}

