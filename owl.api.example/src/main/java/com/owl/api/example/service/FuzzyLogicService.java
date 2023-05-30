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
        app_development.put("worst", fis.getVariable("app_development").getMembership("worst"));
        app_development.put("below_average", fis.getVariable("app_development").getMembership("below_average"));
        app_development.put("average", fis.getVariable("app_development").getMembership("average"));
        app_development.put("above_average", fis.getVariable("app_development").getMembership("above_average"));
        app_development.put("best", fis.getVariable("app_development").getMembership("best"));

        purpose.setAppDevelopment(SetPurposeTypeValue(app_development));

        Map<String, Double> video_games = new HashMap<>();
        video_games.put("worst", fis.getVariable("video_games").getMembership("worst"));
        video_games.put("below_average", fis.getVariable("video_games").getMembership("below_average"));
        video_games.put("average", fis.getVariable("video_games").getMembership("average"));
        video_games.put("above_average", fis.getVariable("video_games").getMembership("above_average"));
        video_games.put("best", fis.getVariable("video_games").getMembership("best"));

        purpose.setVideoGames(SetPurposeTypeValue(video_games));

        return purpose;
    }

    private String SetPurposeTypeValue(Map<String, Double> map) {
        Double highestValue = 0.0;
        String keyForHighestValue = "";
        highestValue = Collections.max(map.values());
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (entry.getValue().equals(highestValue)) {
                keyForHighestValue = entry.getKey();
                break;
            }
        }
        return keyForHighestValue;
    }
}
