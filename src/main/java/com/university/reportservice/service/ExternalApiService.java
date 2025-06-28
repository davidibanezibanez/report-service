package com.university.reportservice.service;

import com.university.reportservice.dto.ExternalApiReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ExternalApiService {

    @Value("${external.api.url}")
    private String externalApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendReportToExternalApi(ExternalApiReportRequest reportRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("User-Agent", "University-Activity-Management-System/1.0");

            HttpEntity<ExternalApiReportRequest> entity = new HttpEntity<>(reportRequest, headers);

            // Como la API es ficticia, simularemos una respuesta exitosa
            log.info("Attempting to send report to external API: {}", externalApiUrl);
            log.info("Report data: Activity '{}' with {} students",
                    reportRequest.getActivityName(), reportRequest.getTotalStudents());

            // Simulamos el envío (la API no existe realmente)
            String simulatedResponse = String.format(
                    "{'status': 'success', 'message': 'Report received for activity %s', 'timestamp': '%s'}",
                    reportRequest.getActivityName(),
                    java.time.LocalDateTime.now()
            );

            log.info("External API response (simulated): {}", simulatedResponse);
            return simulatedResponse;

            // El código real sería:
            /*
            ResponseEntity<String> response = restTemplate.exchange(
                externalApiUrl + "/activity-reports",
                HttpMethod.POST,
                entity,
                String.class
            );

            log.info("Report sent successfully to external API. Response: {}", response.getBody());
            return response.getBody();
            */

        } catch (Exception e) {
            log.error("Failed to send report to external API", e);
            return "Error: " + e.getMessage();
        }
    }
}