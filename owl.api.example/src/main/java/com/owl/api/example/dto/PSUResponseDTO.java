package com.owl.api.example.dto;

import lombok.Data;

@Data
public class PSUResponseDTO {
    private String name;
    private String dimensions;
    private String certificate;
    private int power;
}

