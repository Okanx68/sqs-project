package com.project.service;


import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class BreweryDBServiceTest {

    @Inject
    @RestClient
    BreweryDBService breweryDBService;

    // prüfe, ob Rest-Client Response und Open Brewery DB Response gleich sind
    @Test
    void testRestClient() throws IOException, InterruptedException {
        String testCityName = "Austin";
        int count = 3;

        String clientResponse = breweryDBService.getBreweriesByCity(testCityName, count);

        String actualResponse = getActualResponse(testCityName, count);

        assertEquals(clientResponse, actualResponse);
    }

    // gleiche Response vom Rest-Client und Response von der Open Brewery DB mit einer nicht existierenden Stadt ab
    @Test
    void testRestClientWithNonExistentCity() throws IOException, InterruptedException {
        String testNonExistentCityName = "NonExistentCity";
        int count = 3;

        String clientResponse = breweryDBService.getBreweriesByCity(testNonExistentCityName, count);

        String actualResponse = getActualResponse(testNonExistentCityName, count);

        assertEquals(clientResponse, actualResponse);
    }

    // erhalte Brauereidaten direkt aus der Open Brewery DB
    private String getActualResponse(String city, int count) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String url = String.format("https://api.openbrewerydb.org/v1/breweries?by_city=%s&per_page=%d", city, count);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

}
