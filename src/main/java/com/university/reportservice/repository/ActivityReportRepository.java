package com.university.reportservice.repository;

import com.university.reportservice.entity.ActivityReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivityReportRepository extends JpaRepository<ActivityReport, Long> {

    Optional<ActivityReport> findByActivityId(Long activityId);

    List<ActivityReport> findByActivityType(String activityType);

    List<ActivityReport> findByActivityDate(LocalDate activityDate);

    List<ActivityReport> findByOrganizerContainingIgnoreCase(String organizer);

    List<ActivityReport> findByExternalApiSent(Boolean sent);

    @Query("SELECT ar FROM ActivityReport ar WHERE ar.activityDate BETWEEN :startDate AND :endDate")
    List<ActivityReport> findByActivityDateBetween(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    @Query("SELECT ar FROM ActivityReport ar WHERE ar.reportGeneratedDate BETWEEN :startDate AND :endDate")
    List<ActivityReport> findByReportGeneratedDateBetween(@Param("startDate") LocalDate startDate,
                                                          @Param("endDate") LocalDate endDate);
}