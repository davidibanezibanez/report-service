package com.university.reportservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "activity_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    @Column(name = "activity_name", nullable = false)
    private String activityName;

    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @Column(name = "activity_type", nullable = false)
    private String activityType;

    @Column(nullable = false)
    private String organizer;

    @ElementCollection
    @CollectionTable(name = "completed_students", joinColumns = @JoinColumn(name = "report_id"))
    @Column(name = "student_username")
    private List<String> completedStudents;

    @Column(name = "total_completed_students", nullable = false)
    private Integer totalCompletedStudents;

    @Column(name = "completion_date", nullable = false)
    private LocalDateTime completionDate;

    @Column(name = "report_generated_date", nullable = false)
    private LocalDateTime reportGeneratedDate = LocalDateTime.now();

    @Column(name = "external_api_sent")
    private Boolean externalApiSent = false;

    @Column(name = "external_api_sent_date")
    private LocalDateTime externalApiSentDate;

    @Column(name = "external_api_response")
    private String externalApiResponse;
}