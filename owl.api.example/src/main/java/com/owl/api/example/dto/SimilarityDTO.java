package com.owl.api.example.dto;

import lombok.Data;
import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;

@Data
public class SimilarityDTO implements CaseComponent {
    private String cpuName;
    private int cpuTDP;
    private double cpuClockSpeed;
    private int cpuCores;
    private int cpuThreads;
    private String cpuSocket;
    private String gpuName;
    private int gpuTDP;
    private int gpuCoreClock;
    private int gpuMemoryClock;
    private int gpuVideoMemory;
    private String motherboardFormFactor;
    private String motherboardChipset;
    private String motherboardSocket;
    private String ramType;
    private int ramLatency;
    private double ramVoltage;
    private int ramCapacity;
    private int psuPower;
    private Attribute idAttribute;

    @Override
    public Attribute getIdAttribute() {
        return null;
    }
}
