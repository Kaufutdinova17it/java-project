package com.hfbe_Polina.web_project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Сущность, представляющая визит животного в ветеринарную клинику.
 */
@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата визита.
     */
    @NotNull(message = "Дата визита обязательна")
    @Column(name = "visit_date")
    private LocalDate date;

    /**
     * Время визита (интервал 1 час).
     */
    @NotNull(message = "Время визита обязательно")
    @Column(name = "visit_time")
    private LocalTime time;

    /**
     * Диагноз.
     */
    @NotBlank(message = "Диагноз обязателен")
    private String diagnosis;

    /**
     * Назначенное лечение.
     */
    @NotBlank(message = "Лечение обязательно")
    private String treatment;

    /**
     * Животное, которому принадлежит визит.
     */
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    /**
     * Проверка корректности даты визита.
     * Дата должна быть до 12 марта 2026 года.
     */
    @AssertTrue(message = "Дата визита должна быть до 12 марта 2026 года")
    public boolean isDateValid() {
        if (date == null) return true;
        return !date.isAfter(LocalDate.of(2026, 3, 12));
    }

    // === Геттеры и сеттеры ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
}
