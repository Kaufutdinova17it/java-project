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
 *
 * <p>
 * Класс описывает питомца, зарегистрированного в системе, и содержит основные
 * данные о нём: имя, вид, породу, дату рождения, номер паспорта и владельца.
 * Сущность участвует в двух ключевых связях:
 * </p>
 *
 * <ul>
 *     <li><b>ManyToOne</b> — с владельцем ({@link Owner}), которому принадлежит животное</li>
 *     <li><b>OneToMany</b> — с визитами ({@link Visit}), связанными с данным животным</li>
 * </ul>
 *
 * <p><b>Назначение сущности:</b></p>
 * <ul>
 *     <li>Хранение информации о питомце</li>
 *     <li>Участие в бизнес‑логике, связанной с регистрацией визитов</li>
 *     <li>Обеспечение уникальности паспортного номера</li>
 *     <li>Валидация корректности даты рождения</li>
 * </ul>
 *
 * <p><b>Валидация полей:</b></p>
 * <ul>
 *     <li><b>name</b> — обязательное поле, длина 2–50 символов</li>
 *     <li><b>species</b> — обязательное поле</li>
 *     <li><b>breed</b> — обязательное поле</li>
 *     <li><b>birthDate</b> — обязательная дата, формат yyyy‑MM‑dd</li>
 *     <li><b>passportNumber</b> — обязательное поле, строго 10 цифр, уникальное</li>
 *     <li><b>owner</b> — обязательная ссылка на владельца</li>
 * </ul>
 *
 * <p><b>Особенности реализации:</b></p>
 * <ul>
 *     <li>Поле <code>passportNumber</code> помечено как уникальное на уровне БД</li>
 *     <li>Используется дополнительный прокси‑флаг <code>birthDateValidProxy</code>
 *         для корректной работы Thymeleaf с @AssertTrue‑валидацией</li>
 *     <li>Метод <code>isBirthDateValid()</code> ограничивает дату рождения
 *         январём 2026 года согласно бизнес‑правилам</li>
 *     <li>Связь с визитами настроена с каскадным удалением и orphanRemoval</li>
 * </ul>
 *
 * <p><b>Используется в случаях:</b></p>
 * <ul>
 *     <li>При создании и редактировании животного</li>
 *     <li>При регистрации визита</li>
 *     <li>При отображении информации о владельце и его питомцах</li>
 *     <li>При проверке уникальности паспортного номера</li>
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
    @AssertTrue(message = "Дата рождения должна быть не позже января 2026")
    public boolean isBirthDateValid() {
        return birthDate != null && birthDate.isBefore(LocalDate.of(2026, 2, 1));
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
