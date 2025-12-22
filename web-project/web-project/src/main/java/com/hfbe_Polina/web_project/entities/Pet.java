package com.hfbe_Polina.web_project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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

    /**
     * Уникальный идентификатор животного.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя животного.
     */
    @NotBlank(message = "Имя животного обязательно")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String name;

    /**
     * Вид животного (например, собака, кошка).
     */
    @NotBlank(message = "Вид обязателен")
    private String species;

    /**
     * Порода животного.
     */
    @NotBlank(message = "Порода обязательна")
    private String breed;

    /**
     * Дата рождения животного.
     */
    @NotNull(message = "Дата рождения обязательна")
    private LocalDate birthDate;

    /**
     * Уникальный номер паспорта животного.
     */
    @NotBlank(message = "Номер паспорта обязателен")
    @Column(unique = true)
    private String passportNumber;

    /**
     * Владелец животного.
     */
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    /**
     * Список визитов животного.
     */
    @OneToMany(mappedBy = "pet")
    private List<Visit> visits;

    // Getters and setters


// === Геттеры и сеттеры ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }


    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public Owner getOwner() { return owner; }
    public void setOwner(Owner owner) { this.owner = owner; }

    public List<Visit> getVisits() { return visits; }
    public void setVisits(List<Visit> visits) { this.visits = visits; }

    @Override
    public String toString() {
        return name;
    }
}
