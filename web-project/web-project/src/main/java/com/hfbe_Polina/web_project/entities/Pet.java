package com.hfbe_Polina.web_project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.AssertTrue;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * Сущность, представляющая животное.
 * <p>
 * Хранит данные о животном, включая имя, вид, породу, дату рождения и номер паспорта.
 * Связана с владельцем ({@link Owner}) через отношение ManyToOne.
 * Также имеет связь OneToMany с визитами ({@link Visit}).
 * </p>
 *
 * <p><b>Особенности:</b></p>
 * <ul>
 *     <li>Уникальный номер паспорта животного</li>
 *     <li>Валидация имени, вида и породы</li>
 *     <li>Связь с владельцем обязательна</li>
 * </ul>
 */
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя животного обязательно")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String name;

    @NotBlank(message = "Вид обязателен")
    private String species;

    @NotBlank(message = "Порода обязательна")
    private String breed;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Дата рождения обязательна")
    private LocalDate birthDate;

    @NotBlank(message = "Номер паспорта обязателен")
    @Pattern(regexp = "\\d{10}", message = "Номер паспорта должен содержать ровно 10 цифр")
    @Column(unique = true)
    private String passportNumber;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @NotNull(message = "Владелец обязателен")
    private Owner owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits;



    @Transient
    private boolean birthDateValidProxy = true;

    public boolean isBirthDateValidProxy() {
        if (birthDate == null) return true;
        return !birthDate.isAfter(LocalDate.now());
    }

    public boolean getBirthDateValidProxy() {
        return isBirthDateValidProxy();
    }

    public void setBirthDateValidProxy(boolean ignored) {
        // пустой сеттер обязателен для Thymeleaf
    }

    // === Валидация даты рождения ===
    @AssertTrue(message = "Дата рождения не может быть в будущем")
    public boolean isBirthDateValid() {
        if (birthDate == null) return true; // @NotNull обработает отдельно
        return !birthDate.isAfter(LocalDate.now());
    }

    // === Геттеры и сеттеры ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public Owner getOwner() { return owner; }
    public void setOwner(Owner owner) { this.owner = owner; }

    public List<Visit> getVisits() { return visits; }
    public void setVisits(List<Visit> visits) { this.visits = visits; }

    @Override
    public String toString() {
        return name;
    }
}
