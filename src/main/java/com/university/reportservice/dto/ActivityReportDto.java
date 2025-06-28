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
public class ActivityReportDto {
    private Long id;
    private Long activityId;
    private String activityName;
    private LocalDate activityDate;
    private String activityType;
    private String organizer;
    private List<String> completedStudents;
    private Integer totalCompletedStudents;
    private LocalDateTime completionDate;
    private LocalDateTime reportGeneratedDate;
    private Boolean externalApiSent;
    private LocalDateTime externalApiSentDate;
    private String externalApiResponse;
}