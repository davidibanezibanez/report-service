package com.university.reportservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ActivityCompletionReportRequest(
        @NotNull Long activityId,
        @NotBlank String activityName,
        @NotNull LocalDate activityDate,
        @NotBlank String activityType,
        @NotBlank String organizer,
        @NotNull List<String> completedStudents,
        @NotNull Integer totalCompletedStudents,
        @NotNull LocalDateTime completionDate
) {}