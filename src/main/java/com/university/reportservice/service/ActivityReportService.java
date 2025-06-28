package com.university.reportservice.service;

import com.university.reportservice.dto.ActivityCompletionReportRequest;
import com.university.reportservice.dto.ActivityReportDto;
import com.university.reportservice.dto.ExternalApiReportRequest;
import com.university.reportservice.entity.ActivityReport;
import com.university.reportservice.repository.ActivityReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivityReportService {

    @Autowired
    private ActivityReportRepository activityReportRepository;

    @Autowired
    private ExternalApiService externalApiService;

    public ActivityReportDto processActivityCompletionReport(ActivityCompletionReportRequest request) {
        // Verificar si ya existe un reporte para esta actividad
        if (activityReportRepository.findByActivityId(request.activityId()).isPresent()) {
            throw new RuntimeException("Report for this activity already exists");
        }

        // Crear el reporte
        ActivityReport report = new ActivityReport();
        report.setActivityId(request.activityId());
        report.setActivityName(request.activityName());
        report.setActivityDate(request.activityDate());
        report.setActivityType(request.activityType());
        report.setOrganizer(request.organizer());
        report.setCompletedStudents(request.completedStudents());
        report.setTotalCompletedStudents(request.totalCompletedStudents());
        report.setCompletionDate(request.completionDate());

        // Guardar el reporte
        ActivityReport savedReport = activityReportRepository.save(report);

        log.info("Activity completion report created for activity: {} with {} students",
                request.activityName(), request.totalCompletedStudents());

        // Enviar a la API externa
        sendToExternalApi(savedReport);

        return convertToDto(savedReport);
    }

    private void sendToExternalApi(ActivityReport report) {
        try {
            // Preparar datos para la API externa
            ExternalApiReportRequest externalRequest = new ExternalApiReportRequest();
            externalRequest.setActivityId(report.getActivityId());
            externalRequest.setActivityName(report.getActivityName());
            externalRequest.setActivityDate(report.getActivityDate());
            externalRequest.setActivityType(report.getActivityType());
            externalRequest.setOrganizer(report.getOrganizer());
            externalRequest.setTotalStudents(report.getTotalCompletedStudents());
            externalRequest.setActivityCompletionDate(report.getCompletionDate());
            externalRequest.setReportGenerationDate(report.getReportGeneratedDate());

            // Convertir lista de estudiantes
            List<ExternalApiReportRequest.StudentCompletionData> studentData =
                    report.getCompletedStudents().stream()
                            .map(username -> new ExternalApiReportRequest.StudentCompletionData(username, "COMPLETED"))
                            .collect(Collectors.toList());
            externalRequest.setStudents(studentData);

            // Enviar a la API externa
            String response = externalApiService.sendReportToExternalApi(externalRequest);

            // Actualizar el reporte con la respuesta
            report.setExternalApiSent(true);
            report.setExternalApiSentDate(LocalDateTime.now());
            report.setExternalApiResponse(response);
            activityReportRepository.save(report);

            log.info("Report sent to external API successfully for activity: {}", report.getActivityName());

        } catch (Exception e) {
            log.error("Failed to send report to external API for activity: {}", report.getActivityName(), e);

            // Marcar como no enviado y guardar el error
            report.setExternalApiSent(false);
            report.setExternalApiResponse("Error: " + e.getMessage());
            activityReportRepository.save(report);
        }
    }

    public List<ActivityReportDto> getAllReports() {
        return activityReportRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ActivityReportDto getReportById(Long id) {
        ActivityReport report = activityReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        return convertToDto(report);
    }

    public ActivityReportDto getReportByActivityId(Long activityId) {
        ActivityReport report = activityReportRepository.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("Report not found for activity"));
        return convertToDto(report);
    }

    public List<ActivityReportDto> getReportsByType(String activityType) {
        return activityReportRepository.findByActivityType(activityType).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityReportDto> getReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        return activityReportRepository.findByActivityDateBetween(startDate, endDate).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ActivityReportDto> getPendingExternalApiReports() {
        return activityReportRepository.findByExternalApiSent(false).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ActivityReportDto retryExternalApiSend(Long reportId) {
        ActivityReport report = activityReportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        sendToExternalApi(report);
        return convertToDto(report);
    }

    private ActivityReportDto convertToDto(ActivityReport report) {
        ActivityReportDto dto = new ActivityReportDto();
        dto.setId(report.getId());
        dto.setActivityId(report.getActivityId());
        dto.setActivityName(report.getActivityName());
        dto.setActivityDate(report.getActivityDate());
        dto.setActivityType(report.getActivityType());
        dto.setOrganizer(report.getOrganizer());
        dto.setCompletedStudents(report.getCompletedStudents());
        dto.setTotalCompletedStudents(report.getTotalCompletedStudents());
        dto.setCompletionDate(report.getCompletionDate());
        dto.setReportGeneratedDate(report.getReportGeneratedDate());
        dto.setExternalApiSent(report.getExternalApiSent());
        dto.setExternalApiSentDate(report.getExternalApiSentDate());
        dto.setExternalApiResponse(report.getExternalApiResponse());
        return dto;
    }
}