package com.hfbe_Polina.web_project.entities;
import jakarta.validation.constraints.Pattern;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Сущность, представляющая владельца животного.
 * <p>
 * Хранит персональные данные владельца и список принадлежащих ему животных.
 * Используется в связке с сущностью {@link Pet} (отношение OneToMany).
 * </p>
 *
 * <p><b>Особенности:</b></p>
 * <ul>
 *     <li>Уникальный email владельца</li>
 *     <li>Валидация имени и телефона</li>
 *     <li>Каскадное удаление животных при удалении владельца (если включено)</li>
 * </ul>
 */
@Entity
@Table(name = "owners")
public class Owner {

    /**
     * Уникальный идентификатор владельца.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя владельца.
     */
    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё\\s-]+$", message = "Имя может содержать только буквы, пробелы и дефис")
    private String name;

    /**
     * Email владельца (уникальный).
     */
    @Email(message = "Некорректный формат email")
    @NotBlank(message = "Email обязателен")
    @Column(unique = true)
    private String email;

    /**
     * Номер телефона владельца.
     */
    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^8\\d{10}$", message = "Телефон должен начинаться с 8 и содержать ровно 11 цифр")
    private String phone;


    /**
     * Список животных, принадлежащих владельцу.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets;


    // Getters and setters


// === Геттеры и сеттеры ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Pet> getPets() { return pets; }
    public void setPets(List<Pet> pets) { this.pets = pets; }
}
