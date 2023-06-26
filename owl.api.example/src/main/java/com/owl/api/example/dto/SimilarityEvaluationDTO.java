package com.owl.api.example.dto;

import lombok.Data;

@Data
public class SimilarityEvaluationDTO {
    private SimilarityDTO object;
    private String evaluation;
}
