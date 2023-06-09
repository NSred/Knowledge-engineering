package com.owl.api.example.dto;

import lombok.Data;
@Data
public class CauseProbabilityDTO implements Comparable<CauseProbabilityDTO>{
    private String cause;
    private double probability;

    @Override
    public int compareTo(CauseProbabilityDTO otherCause) {
        return Double.compare( otherCause.getProbability(), this.probability);
    }
}
