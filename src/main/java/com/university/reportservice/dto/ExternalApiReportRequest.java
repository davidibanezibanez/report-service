package com.university.reportservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalApiReportRequest {
    private String source = "University Activity Management System";
    private String reportType = "ACTIVITY_COMPLETION";
    private Long activityId;
    private String activityName;
    private LocalDate activityDate;
    private String activityType;
    private String organizer;
    private List<StudentCompletionData> students;
    private Integer totalStudents;
    private LocalDateTime activityCompletionDate;
    private LocalDateTime reportGenerationDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentCompletionData {
        private String username;
        private String status = "COMPLETED";
    }
}