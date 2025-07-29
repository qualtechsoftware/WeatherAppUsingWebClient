package com.org.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WeatherResponse {

    private Main main;

    private List<Weather> weather;

    @JsonProperty("name")
    private String cityName;

    @Data
    public static class Main {

        @JsonProperty("temp")
        private double temp; // temp in Kelvin from API

        @JsonProperty("humidity")
        private double humidity;

        @JsonProperty("feels_like")
        private double feelsLike; // in Kelvin from API
    }

    @Data
    public static class Weather {

        private String main;

        private String description;

        private String icon;
    }

    @Override
    public String toString() {
        return "Weather in " + cityName + ": " +
                (weather != null && !weather.isEmpty() ? weather.get(0).getDescription() : "N/A") +
                ", Temp: " + (main != null ? Math.round((main.getTemp() - 273.15) * 100) / 100.0 : "N/A") + "°C";
    }
}
