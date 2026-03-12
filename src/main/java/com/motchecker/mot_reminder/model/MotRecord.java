package com.motchecker.mot_reminder.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mot_records", schema = "mot_reminder_db")
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

    // Constructors
    public MotRecord() {}

    public MotRecord(Car car, LocalDate inspectionDate, boolean passed, String notes) {
        this.car = car;
        this.inspectionDate = inspectionDate;
        this.passed = passed;
        this.notes = notes;
    }

    // Getters a Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public LocalDate getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(LocalDate inspectionDate) { this.inspectionDate = inspectionDate; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}