package com.motchecker.mot_reminder.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "mot_records", schema = "mot_reminder_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "inspection_date", nullable = false)
    private LocalDate inspectionDate;

    @Column(name = "passed", nullable = false)
    private boolean passed;

    @Column(name = "notes", length = 500)
    private String notes;
}