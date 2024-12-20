package com.pollu.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor  // Ajout de ce constructeur
public class PollutionDTO {
    private Coord coord;
    private List<PollutionItem> list;

    @Data
    @NoArgsConstructor  // Ajout pour chaque classe interne
    public static class Coord {
        private double lon;
        private double lat;
    }

    @Data
    @NoArgsConstructor
    public static class PollutionItem {
        private Main main;
        private Components components;
        private long dt;
    }

    @Data
    @NoArgsConstructor
    public static class Main {
        private int aqi;
    }

    @Data
    @NoArgsConstructor
    public static class Components {
        private double co;
        private double no;
        private double no2;
        private double o3;
        private double so2;
        private double pm2_5;
        private double pm10;
        private double nh3;
    }
}