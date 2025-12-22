package com.hfbe_Polina.web_project.repositories;

import com.hfbe_Polina.web_project.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с сущностью {@link Visit}.
 * <p>
 * Предоставляет стандартные CRUD‑операции через интерфейс {@link JpaRepository}.
 * Используется сервисным слоем {@link com.hfbe_Polina.web_project.services.VisitService}.
 * </p>
 *
 * <p><b>Основные возможности:</b></p>
 * <ul>
 *     <li>Поиск визитов</li>
 *     <li>Сохранение визитов</li>
 *     <li>Удаление визитов</li>
 * </ul>
 */
@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
}
