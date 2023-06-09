package com.owl.api.example.service;

import com.owl.api.example.dto.CauseProbabilityDTO;
import org.springframework.stereotype.Service;
import unbbayes.io.BaseIO;
import unbbayes.io.NetIO;
import unbbayes.prs.Node;
import unbbayes.prs.bn.JunctionTreeAlgorithm;
import unbbayes.prs.bn.ProbabilisticNetwork;
import unbbayes.prs.bn.ProbabilisticNode;
import unbbayes.util.extension.bn.inference.IInferenceAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BayesService {
    private ProbabilisticNetwork net;
    private IInferenceAlgorithm algorithm;

    public BayesService() throws IOException {
        BaseIO io = new NetIO();
        net = (ProbabilisticNetwork) io.load(new File("data/Bayes.net"));
    }
    public List<CauseProbabilityDTO> getAllProbabilities(List<String> nodes) throws Exception {
        algorithm = new JunctionTreeAlgorithm();
        algorithm.setNetwork(net);
        algorithm.run();

        for(String node : nodes){
            ProbabilisticNode factNode = (ProbabilisticNode)net.getNode(node);
            int stateIndex = 0;
            if(factNode == null)
                throw new Exception("Unknown cause of malfunction!");
            factNode.addFinding(stateIndex);
        }

        try {
            net.updateEvidences();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        List<CauseProbabilityDTO> causeProbabilityDTOs = new ArrayList<>();

        for(String node : nodes){
            for (Node parent : net.getNode(node).getParents()){
                CauseProbabilityDTO causeProbabilityDTO1 = new CauseProbabilityDTO();
                causeProbabilityDTO1.setCause(parent.getName());
                causeProbabilityDTO1.setProbability(Math.round(((ProbabilisticNode) parent).getMarginalAt(0) * 100));
                causeProbabilityDTOs.add(causeProbabilityDTO1);
                for (Node grandparent : net.getNode(parent.getName()).getParents()){
                    CauseProbabilityDTO causeProbabilityDTO2 = new CauseProbabilityDTO();
                    causeProbabilityDTO2.setCause(grandparent.getName());
                    causeProbabilityDTO2.setProbability(Math.round(((ProbabilisticNode) grandparent).getMarginalAt(0) * 100));
                    causeProbabilityDTOs.add(causeProbabilityDTO2);

                }
            }
        }

        if(causeProbabilityDTOs.isEmpty())
            throw new Exception("Unknown cause of malfunction!");

        causeProbabilityDTOs = causeProbabilityDTOs.stream().distinct().collect(Collectors.toList());
        for(String node : nodes) {
            Iterator<CauseProbabilityDTO> iterator = causeProbabilityDTOs.iterator();
            while (iterator.hasNext()) {
                CauseProbabilityDTO obj = iterator.next();
                if (obj.getCause().equals(node)) {
                    iterator.remove();
                }
            }
        }
        Collections.sort(causeProbabilityDTOs);
        return causeProbabilityDTOs.subList(0, Math.min(causeProbabilityDTOs.size(), 5));
    }
}
