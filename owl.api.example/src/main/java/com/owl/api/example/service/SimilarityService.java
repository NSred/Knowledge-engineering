package com.owl.api.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.owl.api.example.connector.CsvConnector;
import com.owl.api.example.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ucm.gaia.jcolibri.casebase.LinealCaseBase;
import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;
import ucm.gaia.jcolibri.cbrcore.*;
import ucm.gaia.jcolibri.exception.ExecutionException;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.MaxString;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Threshold;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;
import ucm.gaia.jcolibri.method.retrieve.selection.SelectCases;

import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SimilarityService implements StandardCBRApplication {
    private Connector _connector;
    private CBRCaseBase _caseBase;
    private NNConfig simConfig;
    private String ram = "";
    private String psu = "";
    private String motherboard = "";
    private SimilarityDTO similarityDTO = new SimilarityDTO();

    private final RAMService ramService;
    private final PSUService psuService;
    private final CPUService cpuService;
    private final GPUService gpuService;
    private final MotherboardService motherboardService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public SimilarityService(RAMService ramService, PSUService psuService, CPUService cpuService, GPUService gpuService, MotherboardService motherboardService, ModelMapper modelMapper, ObjectMapper objectMapper){
        this.ramService = ramService;
        this.psuService = psuService;
        this.cpuService = cpuService;
        this.gpuService = gpuService;
        this.motherboardService = motherboardService;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    public void configure() {
        _connector =  new CsvConnector();

        _caseBase = new LinealCaseBase();

        simConfig = new NNConfig();
        simConfig.setDescriptionSimFunction(new Average());

        simConfig.addMapping(new Attribute("cpuName", SimilarityDTO.class), new MaxString());
        simConfig.addMapping(new Attribute("cpuClockSpeed", SimilarityDTO.class), new Equal());
        simConfig.addMapping(new Attribute("cpuCores", SimilarityDTO.class), new Threshold(1));
        simConfig.addMapping(new Attribute("cpuThreads", SimilarityDTO.class), new Threshold(1));
        simConfig.addMapping(new Attribute("cpuSocket", SimilarityDTO.class), new Equal());
        simConfig.addMapping(new Attribute("cpuTDP", SimilarityDTO.class), new Threshold(5));

        simConfig.addMapping(new Attribute("gpuName", SimilarityDTO.class), new MaxString());
        simConfig.addMapping(new Attribute("gpuCoreClock", SimilarityDTO.class), new Threshold(100));
        simConfig.addMapping(new Attribute("gpuMemoryClock", SimilarityDTO.class), new Threshold(100));
        simConfig.addMapping(new Attribute("gpuVideoMemory", SimilarityDTO.class), new Threshold(1000));
        simConfig.addMapping(new Attribute("gpuTDP", SimilarityDTO.class), new Threshold(5));

        if(!this.motherboard.isEmpty()){
            setConfig1();
        }

        if(!this.ram.isEmpty()){
            setConfig2();
        }

        if(!this.psu.isEmpty()){
            setConfig3();
        }
    }

    private void setConfig1(){
        simConfig.addMapping(new Attribute("motherboardFormFactor", SimilarityDTO.class), new Equal());
        simConfig.addMapping(new Attribute("motherboardChipset", SimilarityDTO.class), new Equal());
        simConfig.addMapping(new Attribute("motherboardSocket", SimilarityDTO.class), new Equal());
    }

    private void setConfig2(){
        simConfig.addMapping(new Attribute("ramType", SimilarityDTO.class), new Equal());
        simConfig.addMapping(new Attribute("ramLatency", SimilarityDTO.class), new Threshold(2));
        simConfig.addMapping(new Attribute("ramVoltage", SimilarityDTO.class), new Equal());
        simConfig.addMapping(new Attribute("ramCapacity", SimilarityDTO.class), new Equal());
    }

    private void setConfig3(){
        simConfig.addMapping(new Attribute("psuPower", SimilarityDTO.class), new Threshold(50));
    }

    @Override
    public CBRCaseBase preCycle() throws ExecutionException {
        _caseBase.init(_connector);
        java.util.Collection<CBRCase> cases = _caseBase.getCases();
        return _caseBase;
    }

    @Override
    public void cycle(CBRQuery cbrQuery) throws ExecutionException {
        Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(_caseBase.getCases(), cbrQuery, simConfig);
        eval = SelectCases.selectTopKRR(eval, 5);
        List<SimilarityEvaluationDTO> similarityEvaluationDTOS = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        for(RetrievalResult res : eval){
            SimilarityEvaluationDTO similarityEvaluationDTO = new SimilarityEvaluationDTO();
            similarityEvaluationDTO.setObject(modelMapper.map(res.get_case().getDescription(), SimilarityDTO.class));
            similarityEvaluationDTO.setEvaluation(Double.parseDouble(df.format(res.getEval()*100))+"%");
            similarityEvaluationDTOS.add(similarityEvaluationDTO);
        }
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(similarityEvaluationDTOS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        try (FileWriter fileWriter = new FileWriter("data/Results.txt")) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postCycle() throws ExecutionException {
    }

    public void main(String cpu, String gpu, String motherboard, String ram, String psu)
    {
        this.psu = psu;
        this.motherboard = motherboard;
        this.ram = ram;
        StandardCBRApplication recommender = new SimilarityService(ramService, psuService, cpuService, gpuService, motherboardService, modelMapper, objectMapper);
        try {
            recommender.configure();

            recommender.preCycle();

            CBRQuery query = new CBRQuery();

            List<CPUResponseDTO> cpuResponseDTOS = this.cpuService.getAllCPUs();
            for(CPUResponseDTO cpuDTO : cpuResponseDTOS){
                if(cpuDTO.getName().equals(cpu)){
                    similarityDTO.setCpuName(cpuDTO.getName());
                    similarityDTO.setCpuClockSpeed(cpuDTO.getClockSpeed());
                    similarityDTO.setCpuCores(cpuDTO.getCores());
                    similarityDTO.setCpuThreads(cpuDTO.getThreads());
                    similarityDTO.setCpuSocket(cpuDTO.getSocket());
                    similarityDTO.setCpuTDP(cpuDTO.getTDP());
                }
            }

            List<GPUResponseDTO> gpuResponseDTOS = this.gpuService.getAllGPUs();
            for(GPUResponseDTO gpuDTO : gpuResponseDTOS){
                if(gpuDTO.getName().equals(gpu)){
                    similarityDTO.setGpuName(gpuDTO.getName());
                    similarityDTO.setGpuCoreClock(gpuDTO.getCoreClock());
                    similarityDTO.setGpuMemoryClock(gpuDTO.getMemoryClock());
                    similarityDTO.setGpuVideoMemory(gpuDTO.getVideoMemory());
                    similarityDTO.setGpuTDP(gpuDTO.getTDP());
                }
            }

            List<MotherboardResponseDTO> motherboardResponseDTOS = this.motherboardService.getAllMotherboards();
            for(MotherboardResponseDTO motherboardDTO : motherboardResponseDTOS){
                if(motherboardDTO.getName().equals(motherboard)){
                    similarityDTO.setMotherboardFormFactor(motherboardDTO.getFormFactor());
                    similarityDTO.setMotherboardChipset(motherboardDTO.getChipset());
                    similarityDTO.setMotherboardSocket(motherboardDTO.getSocket());
                }
            }

            List<RAMResponseDTO> ramResponseDTOS = this.ramService.getAllRAMs();
            for(RAMResponseDTO ramDTO : ramResponseDTOS){
                if(ramDTO.getName().equals(ram)){
                    similarityDTO.setRamType(ramDTO.getType());
                    similarityDTO.setRamLatency(ramDTO.getLatency());
                    similarityDTO.setRamVoltage(ramDTO.getVoltage());
                    similarityDTO.setRamCapacity(ramDTO.getCapacity());
                }
            }

            List<PSUResponseDTO> psuResponseDTOS = this.psuService.getAllPSUs();
            for(PSUResponseDTO psuDTO : psuResponseDTOS){
                if(psuDTO.getName().equals(psu)){
                    similarityDTO.setPsuPower(psuDTO.getPower());
                }
            }

            query.setDescription(similarityDTO);

            recommender.cycle(query);

            if(!ram.isEmpty() && !motherboard.isEmpty() && !psu.isEmpty()) {
                try (CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter("data/Similarity.csv", true))
                        .withSeparator(';')
                        .withQuoteChar(CSVWriter.NO_QUOTE_CHARACTER)
                        .build()) {
                    String[] data = {
                            similarityDTO.getCpuName(),
                            String.valueOf(similarityDTO.getCpuClockSpeed()),
                            String.valueOf(similarityDTO.getCpuCores()),
                            String.valueOf(similarityDTO.getCpuThreads()),
                            similarityDTO.getCpuSocket(),
                            String.valueOf(similarityDTO.getCpuTDP()),
                            similarityDTO.getGpuName(),
                            String.valueOf(similarityDTO.getGpuCoreClock()),
                            String.valueOf(similarityDTO.getGpuMemoryClock()),
                            String.valueOf(similarityDTO.getGpuVideoMemory()),
                            String.valueOf(similarityDTO.getGpuTDP()),
                            similarityDTO.getMotherboardFormFactor(),
                            similarityDTO.getMotherboardChipset(),
                            similarityDTO.getMotherboardSocket(),
                            similarityDTO.getRamType(),
                            String.valueOf(similarityDTO.getRamLatency()),
                            String.valueOf(similarityDTO.getRamVoltage()),
                            String.valueOf(similarityDTO.getRamCapacity()),
                            String.valueOf(similarityDTO.getPsuPower())
                    };
                    writer.writeNext(data);
                } catch (IOException e) {
                    // Handle the exception appropriately
                    e.printStackTrace();
                }
            }

            recommender.postCycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
