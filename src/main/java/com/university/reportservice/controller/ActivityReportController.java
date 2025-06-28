package com.university.reportservice.controller;

import com.university.reportservice.dto.ActivityCompletionReportRequest;
import com.university.reportservice.dto.ActivityReportDto;
import com.university.reportservice.service.ActivityReportService;
import com.university.reportservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ActivityReportController {

    @Autowired
    private ActivityReportService activityReportService;

    @Autowired
    private AuthService authService;

    private boolean isAuthorized(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        Map<String, Object> tokenValidation = authService.validateToken(authHeader);
        return (Boolean) tokenValidation.getOrDefault("valid", false);
    }

    @PostMapping("/activity-completion")
    public ResponseEntity<ActivityReportDto> createActivityCompletionReport(
            @Valid @RequestBody ActivityCompletionReportRequest request) {
        // Este endpoint es llamado internamente por el activity-service
        // No requiere autenticación ya que es comunicación entre microservicios
        try {
            ActivityReportDto report = activityReportService.processActivityCompletionReport(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ActivityReportDto>> getAllReports(
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ActivityReportDto> reports = activityReportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityReportDto> getReportById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            ActivityReportDto report = activityReportService.getReportById(id);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<ActivityReportDto> getReportByActivityId(
            @PathVariable Long activityId,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            ActivityReportDto report = activityReportService.getReportByActivityId(activityId);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/type/{activityType}")
    public ResponseEntity<List<ActivityReportDto>> getReportsByType(
            @PathVariable String activityType,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ActivityReportDto> reports = activityReportService.getReportsByType(activityType);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ActivityReportDto>> getReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ActivityReportDto> reports = activityReportService.getReportsByDateRange(startDate, endDate);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/pending-external-api")
    public ResponseEntity<List<ActivityReportDto>> getPendingExternalApiReports(
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ActivityReportDto> reports = activityReportService.getPendingExternalApiReports();
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/{id}/retry-external-api")
    public ResponseEntity<ActivityReportDto> retryExternalApiSend(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        if (!isAuthorized(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            ActivityReportDto report = activityReportService.retryExternalApiSend(id);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}