package com.hfbe_Polina.web_project.repositories;

import com.hfbe_Polina.web_project.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link Owner}.
 * <p>
 * Предоставляет стандартные CRUD‑операции через интерфейс {@link JpaRepository}.
 * Используется сервисным слоем {@link com.hfbe_Polina.web_project.services.OwnerService}.
 * </p>
 *
 * <p><b>Основные возможности:</b></p>
 * <ul>
 *     <li>Поиск владельцев</li>
 *     <li>Сохранение владельцев</li>
 *     <li>Удаление владельцев</li>
 *     <li>Поиск по email </li>
 * </ul>
 */

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    boolean existsByEmail(String email);

    // Пример кастомного метода (если используется):
    // Optional<Owner> findByEmail(String email);
}
