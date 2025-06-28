package com.university.reportservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthService {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> validateToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    authServiceUrl + "/auth/validate-token",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
            return response.getBody();
        } catch (Exception e) {
            return Map.of("valid", false);
        }
    }
}