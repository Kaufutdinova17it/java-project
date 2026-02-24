package com.hfbe_Polina.web_project.repositories;

import com.hfbe_Polina.web_project.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link Pet}.
 * <p>
 * Предоставляет стандартные CRUD‑операции через интерфейс {@link JpaRepository}.
 * Используется сервисным слоем {@link com.hfbe_Polina.web_project.services.PetService}.
 * </p>
 *
 * <p><b>Основные возможности:</b></p>
 * <ul>
 *     <li>Поиск животных</li>
 *     <li>Сохранение животных</li>
 *     <li>Удаление животных</li>
 *     <li>Поиск по номеру паспорта </li>
 * </ul>
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    // Пример кастомного метода (если используется):
    // Optional<Pet> findByPassportNumber(String passportNumber);
}
