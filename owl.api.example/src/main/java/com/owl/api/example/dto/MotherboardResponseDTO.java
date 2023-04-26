package com.owl.api.example.dto;

import lombok.Data;

@Data
public class MotherboardResponseDTO {
    private String name;
    private String formFactor;
    private String dimensions;
    private String chipset;
    private String socket;
}

