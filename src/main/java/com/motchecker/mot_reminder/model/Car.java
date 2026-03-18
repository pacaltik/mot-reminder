package com.motchecker.mot_reminder.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "license_plate"})
})
@SQLDelete(sql = "UPDATE cars SET active = false WHERE id=?")
@SQLRestriction("active = true")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^(([ABCDEFHIJKLMNPRSTUVXYZ]|[0-9])-?){5,8}$", message = "Invalid format. Check license plate.")
    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @NotNull(message = "Expiry date is required")
    @FutureOrPresent(message = "Expiry date must be today or in the future")
    @Column(name = "mot_expiry_date", nullable = false)
    private LocalDate motExpiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MotRecord> motHistory = new ArrayList<>();
}