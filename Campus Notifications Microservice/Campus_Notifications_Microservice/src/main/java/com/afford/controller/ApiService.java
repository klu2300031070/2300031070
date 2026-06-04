package com.afford.controller;
import java.net.http.HttpHeaders;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
public class ApiService {

    private final RestClient restClient = RestClient.create();

    public String fetchData() {

        String token = "";

        try {

            return restClient.get()
                    .uri("http://4.224.186.213/evaluation-service/notifications")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(String.class);

        } catch (HttpClientErrorException ex) {

            return ex.getResponseBodyAsString();
        }
    }
}