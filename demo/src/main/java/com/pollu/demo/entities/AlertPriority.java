// AlertPriority.java
package com.pollu.demo.entities;

public enum AlertPriority {
    LEVEL_1(1, "Bon", "#00e400"),
    LEVEL_2(2, "Modéré", "#ffff00"),
    LEVEL_3(3, "Malsain", "#ff7e00"),
    LEVEL_4(4, "Dangereux", "#ff0000"),
    LEVEL_5(5, "Très Dangereux", "#8f3f97");

    private final int level;
    private final String label;
    private final String color;

    AlertPriority(int level, String label, String color) {
        this.level = level;
        this.label = label;
        this.color = color;
    }

    public int getLevel() {
        return level;
    }

    public String getLabel() {
        return label;
    }

    public String getColor() {
        return color;
    }

    public static AlertPriority fromAQI(int aqi) {
        if (aqi >= 301) return LEVEL_5;
        if (aqi >= 201) return LEVEL_4;
        if (aqi >= 151) return LEVEL_3;
        if (aqi >= 101) return LEVEL_2;
        return LEVEL_1;
    }

    public static AlertPriority fromLevel(int level) {
        for (AlertPriority priority : values()) {
            if (priority.getLevel() == level) {
                return priority;
            }
        }
        return LEVEL_1;
    }
}