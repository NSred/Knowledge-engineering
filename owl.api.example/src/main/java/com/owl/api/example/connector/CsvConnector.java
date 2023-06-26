package com.owl.api.example.connector;

import com.owl.api.example.dto.SimilarityDTO;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CaseBaseFilter;
import ucm.gaia.jcolibri.cbrcore.Connector;
import ucm.gaia.jcolibri.exception.InitializingException;
import ucm.gaia.jcolibri.util.FileIO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;

public class CsvConnector  implements Connector {
    @Override
    public void initFromXMLfile(URL url) throws InitializingException {

    }

    @Override
    public void close() {

    }

    @Override
    public void storeCases(Collection<CBRCase> collection) {

    }

    @Override
    public void deleteCases(Collection<CBRCase> collection) {

    }

    @Override
    public Collection<CBRCase> retrieveSomeCases(CaseBaseFilter caseBaseFilter) {
        return null;
    }

    @Override
    public Collection<CBRCase> retrieveAllCases() {
        LinkedList<CBRCase> cases = new LinkedList<CBRCase>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(FileIO.openFile("data/Similarity.csv")));

            String line = "";
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || (line.length() == 0))
                    continue;
                String[] values = line.split(";");

                CBRCase cbrCase = new CBRCase();

                SimilarityDTO similarityDTO = new SimilarityDTO();

                similarityDTO.setCpuName(values[0]);
                similarityDTO.setCpuClockSpeed(Double.parseDouble(values[1]));
                similarityDTO.setCpuCores(Integer.parseInt(values[2]));
                similarityDTO.setCpuThreads(Integer.parseInt(values[3]));
                similarityDTO.setCpuSocket(values[4]);
                similarityDTO.setCpuTDP(Integer.parseInt(values[5]));
                similarityDTO.setGpuName(values[6]);
                similarityDTO.setGpuCoreClock(Integer.parseInt(values[7]));
                similarityDTO.setGpuMemoryClock(Integer.parseInt(values[8]));
                similarityDTO.setGpuVideoMemory(Integer.parseInt(values[9]));
                similarityDTO.setGpuTDP(Integer.parseInt(values[10]));
                similarityDTO.setMotherboardFormFactor(values[11]);
                similarityDTO.setMotherboardChipset(values[12]);
                similarityDTO.setMotherboardSocket(values[13]);
                similarityDTO.setRamType(values[14]);
                similarityDTO.setRamLatency(Integer.parseInt(values[15]));
                similarityDTO.setRamVoltage(Double.parseDouble(values[16]));
                similarityDTO.setRamCapacity(Integer.parseInt(values[17]));
                similarityDTO.setPsuPower(Integer.parseInt(values[18]));

                cbrCase.setDescription(similarityDTO);
                cases.add(cbrCase);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cases;
    }
}
