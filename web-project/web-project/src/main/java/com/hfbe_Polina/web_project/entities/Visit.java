package com.hfbe_Polina.web_project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;

import java.time.LocalDate;

/**
 * Сущность, представляющая визит животного в ветеринарную клинику.
 * <p>
 * Хранит данные о дате визита, диагнозе, назначенном лечении и связанном животном.
 * Связана с сущностью {@link Pet} через отношение ManyToOne.
 * </p>
 *
 * <p><b>Особенности:</b></p>
 * <ul>
 *     <li>Валидация даты визита (должна быть в диапазоне 2000–2025)</li>
 *     <li>Обязательная связь с животным</li>
 *     <li>Используется в контроллере VisitController</li>
 * </ul>
 */
@Entity
@Table(name = "visits")
public class Visit {

    /**
     * Уникальный идентификатор визита.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата визита.
     */
    @NotNull(message = "Дата визита обязательна")
    private LocalDate visitDate;

    /**
     * Диагноз, поставленный ветеринаром.
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
     * Проверка корректности диапазона дат визита.
     * <p>
     * Дата должна быть между 2000 и 2025 годами. Если дата отсутствует,
     * проверка передаётся аннотации {@link NotNull}.
     * </p>
     *
     * @return true, если дата в допустимом диапазоне
     */
    @AssertTrue(message = "Дата визита должна быть между 2000 и 2025 годами")
    public boolean isVisitDateValid() {
        if (visitDate == null) return true;
        int year = visitDate.getYear();
        return year >= 2000 && year <= 2025;


    // Getters and setters
}


// === Геттеры и сеттеры ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
}
