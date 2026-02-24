/**
 * Репозиторий для работы с сущностью {@link Visit}.
 *
 * <p>
 * Интерфейс расширяет {@link JpaRepository}, предоставляя стандартные CRUD‑операции,
 * а также содержит дополнительные методы, связанные с бизнес‑логикой расписания визитов.
 * Репозиторий используется сервисным слоем {@link com.hfbe_Polina.web_project.services.VisitService}
 * для проверки доступности времени, подсчёта визитов и предотвращения пересечений.
 * </p>
 *
 * <p><b>Назначение репозитория:</b></p>
 * <ul>
 *     <li>Получение и сохранение визитов</li>
 *     <li>Подсчёт количества визитов на выбранную дату</li>
 *     <li>Проверка пересечения визитов по времени</li>
 * </ul>
 *
 * <p><b>Особенности реализации:</b></p>
 * <ul>
 *     <li>Визит считается длительностью 1 час</li>
 *     <li>Метод {@code hasOverlappingVisit()} реализован через нативный SQL‑запрос</li>
 *     <li>Проверка пересечения выполняется по правилу:
 *         <br>существующий визит пересекается с новым, если:
 *         <br><code>existing.start < new.end AND new.start < existing.end</code>
 *     </li>
 *     <li>Используются параметры {@link LocalDate} и {@link LocalTime}</li>
 * </ul>
 *
 * <p><b>Используется в случаях:</b></p>
 * <ul>
 *     <li>При создании нового визита</li>
 *     <li>При редактировании существующего визита</li>
 *     <li>При проверке доступности времени</li>
 *     <li>При ограничении количества визитов в день</li>
 * </ul>
 */

package com.hfbe_Polina.web_project.repositories;

import com.hfbe_Polina.web_project.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    // Проверка: сколько визитов уже записано на этот день
    int countByDate(LocalDate date);

    // Проверка пересечения визитов (1 час)
    @Query(
            value = """
            SELECT EXISTS(
                SELECT 1
                FROM visits v
                WHERE v.visit_date = :date
                  AND v.visit_time < :newEndTime
                  AND :newStartTime < v.visit_time + INTERVAL '1 hour'
            )
            """,
            nativeQuery = true
    )
    boolean hasOverlappingVisit(
            @Param("date") LocalDate date,
            @Param("newStartTime") LocalTime newStartTime,
            @Param("newEndTime") LocalTime newEndTime
    );



}
