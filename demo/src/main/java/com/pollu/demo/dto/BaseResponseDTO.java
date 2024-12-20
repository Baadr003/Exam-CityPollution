package com.pollu.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class BaseResponseDTO {
    private Coord coord;
    private List<PollutionData> list;

    @Data
    public static class Coord {
        private double lon;
        private double lat;
    }

    @Data 
    public static class PollutionData {
        private long dt;
        private Main main;
        private Components components;
    }

    @Data
    public static class Main {
        private int aqi;
    }

    @Data
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