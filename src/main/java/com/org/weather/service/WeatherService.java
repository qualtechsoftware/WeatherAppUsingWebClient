package com.org.weather.service;

import com.org.weather.model.WeatherResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {

    private final WebClient webClient;

    @Value("${weather.api.key}")
    private String apiKey;

    public WeatherService(@Value("${weather.api.url}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<WeatherResponse> getWeather(String city) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> Mono.error(new RuntimeException("City not found or invalid API key")))
                .onStatus(status -> status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("Weather API server error")))
                .bodyToMono(WeatherResponse.class)
                .map(this::convertToCelsius);
    }

    private WeatherResponse convertToCelsius(WeatherResponse weather) {
        if (weather.getMain() != null) {
            WeatherResponse.Main main = weather.getMain();
            main.setTemp(main.getTemp() - 273.15);
            main.setFeelsLike(main.getFeelsLike() - 273.15);
        }
        return weather;
    }
}
