package com.owl.api.example.service;

import com.owl.api.example.dto.PurposeTypeDTO;
import net.sourceforge.jFuzzyLogic.FIS;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class FuzzyLogicService {
    private FIS fis;

    public FuzzyLogicService() {
        fis = FIS.load("data/PurposeType.fcl",true);
        if( fis == null ) {
            System.err.println("Can't load file: data/PurposeType.fcl");
        }
    }

    public PurposeTypeDTO getPurposeType(double cpu_clock_speed_ghz, int ram_capacity_gb){
        PurposeTypeDTO purpose = new PurposeTypeDTO();

        fis.setVariable("cpu_clock_speed_ghz", cpu_clock_speed_ghz);
        fis.setVariable("ram_capacity_gb", ram_capacity_gb);

        fis.evaluate();

        Map<String, Double> app_development = new HashMap<>();
        app_development.put("bad", fis.getVariable("app_development").getMembership("bad"));
        app_development.put("average", fis.getVariable("app_development").getMembership("average"));
        app_development.put("excellent", fis.getVariable("app_development").getMembership("excellent"));

        System.out.println("APP DEV");
        purpose.setAppDevelopment(SetPurposeTypeValue(app_development));

        Map<String, Double> video_games = new HashMap<>();
        video_games.put("bad", fis.getVariable("video_games").getMembership("bad"));
        video_games.put("average", fis.getVariable("video_games").getMembership("average"));
        video_games.put("excellent", fis.getVariable("video_games").getMembership("excellent"));

        System.out.println("VIDEO GAMES");
        purpose.setVideoGames(SetPurposeTypeValue(video_games));

        return purpose;
    }

    private String SetPurposeTypeValue(Map<String, Double> map) {
        Double highestValue = 0.0;
        String keyForHighestValue = "";
        highestValue = Collections.max(map.values());
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", value: " + entry.getValue());
            if (entry.getValue().equals(highestValue)) {
                keyForHighestValue = entry.getKey();
                break;
            }
        }
        return keyForHighestValue;
    }
}
