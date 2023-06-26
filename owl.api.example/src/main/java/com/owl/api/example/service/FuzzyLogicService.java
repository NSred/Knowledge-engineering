package com.owl.api.example.service;

import com.owl.api.example.dto.GroupMembershipDTO;
import com.owl.api.example.dto.PurposeTypeDTO;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;

@Service
public class FuzzyLogicService {
    private FIS fis;

    public FuzzyLogicService() {
        fis = FIS.load("data/PurposeType.fcl",true);
        if( fis == null ) {
            System.err.println("Can't load file: data/PurposeType.fcl");
        }
    }

    public PurposeTypeDTO getPurposeType(double cpu_clock_speed_ghz, int ram_capacity_gb, int cpu_cores, int cpu_threads, double gpu_video_memory_gb,
                                         int gpu_core_clock_mhz, double hard_drive_capacity_gb, int psu_power_watts, int l3_size_mb, int ram_latency_ns){
        PurposeTypeDTO purpose = new PurposeTypeDTO();
        GroupMembershipDTO ADMemberships = new GroupMembershipDTO();
        GroupMembershipDTO VGMemberships = new GroupMembershipDTO();
        GroupMembershipDTO MMemberships = new GroupMembershipDTO();
        GroupMembershipDTO HMemberships = new GroupMembershipDTO();
        GroupMembershipDTO HMMemberships = new GroupMembershipDTO();
        GroupMembershipDTO BMemberships = new GroupMembershipDTO();

        //JFuzzyChart.get().chart(fis);

        fis.setVariable("cpu_clock_speed_ghz", cpu_clock_speed_ghz);
        fis.setVariable("ram_capacity_gb", ram_capacity_gb);
        fis.setVariable("cpu_cores", cpu_cores);
        fis.setVariable("cpu_threads", cpu_threads);
        fis.setVariable("gpu_video_memory_gb", gpu_video_memory_gb);
        fis.setVariable("gpu_core_clock_mhz", gpu_core_clock_mhz);
        fis.setVariable("hard_drive_capacity_gb", hard_drive_capacity_gb);
        fis.setVariable("psu_power_watts", psu_power_watts);
        fis.setVariable("l3_size_mb", l3_size_mb);
        fis.setVariable("ram_latency_ns", ram_latency_ns);

        fis.evaluate();

        Variable ad = fis.getVariable("app_development");
        //JFuzzyChart.get().chart(ad, ad.getDefuzzifier(), true);
        Variable vg = fis.getVariable("video_games");
        //JFuzzyChart.get().chart(vg, vg.getDefuzzifier(), true);
        Variable m = fis.getVariable("mining");
        //JFuzzyChart.get().chart(m, m.getDefuzzifier(), true);
        Variable v = fis.getVariable("hosting");
        //JFuzzyChart.get().chart(v, v.getDefuzzifier(), true);
        Variable hm = fis.getVariable("home");
        //JFuzzyChart.get().chart(hm, hm.getDefuzzifier(), true);
        Variable b = fis.getVariable("business");
        //JFuzzyChart.get().chart(b, b.getDefuzzifier(), true);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        ADMemberships.setBad(Double.parseDouble(df.format(fis.getVariable("app_development").getMembership("bad")*100)));
        ADMemberships.setAverage(Double.parseDouble(df.format(fis.getVariable("app_development").getMembership("average")*100)));
        ADMemberships.setExcellent(Double.parseDouble(df.format(fis.getVariable("app_development").getMembership("excellent")*100)));
        purpose.setAppDevelopment(ADMemberships);

        VGMemberships.setBad(Double.parseDouble(df.format(fis.getVariable("video_games").getMembership("bad")*100)));
        VGMemberships.setAverage(Double.parseDouble(df.format(fis.getVariable("video_games").getMembership("average")*100)));
        VGMemberships.setExcellent(Double.parseDouble(df.format(fis.getVariable("video_games").getMembership("excellent")*100)));
        purpose.setVideoGames(VGMemberships);

        MMemberships.setBad(Double.parseDouble(df.format(fis.getVariable("mining").getMembership("bad")*100)));
        MMemberships.setAverage(Double.parseDouble(df.format(fis.getVariable("mining").getMembership("average")*100)));
        MMemberships.setExcellent(Double.parseDouble(df.format(fis.getVariable("mining").getMembership("excellent")*100)));
        purpose.setMining(MMemberships);

        HMemberships.setBad(Double.parseDouble(df.format(fis.getVariable("hosting").getMembership("bad")*100)));
        HMemberships.setAverage(Double.parseDouble(df.format(fis.getVariable("hosting").getMembership("average")*100)));
        HMemberships.setExcellent(Double.parseDouble(df.format(fis.getVariable("hosting").getMembership("excellent")*100)));
        purpose.setHosting(HMemberships);

        HMMemberships.setBad(Double.parseDouble(df.format(fis.getVariable("home").getMembership("bad")*100)));
        HMMemberships.setAverage(Double.parseDouble(df.format(fis.getVariable("home").getMembership("average")*100)));
        HMMemberships.setExcellent(Double.parseDouble(df.format(fis.getVariable("home").getMembership("excellent")*100)));
        purpose.setHome(HMMemberships);

        BMemberships.setBad(Double.parseDouble(df.format(fis.getVariable("business").getMembership("bad")*100)));
        BMemberships.setAverage(Double.parseDouble(df.format(fis.getVariable("business").getMembership("average")*100)));
        BMemberships.setExcellent(Double.parseDouble(df.format(fis.getVariable("business").getMembership("excellent")*100)));
        purpose.setBusiness(BMemberships);

        return purpose;
    }
}
