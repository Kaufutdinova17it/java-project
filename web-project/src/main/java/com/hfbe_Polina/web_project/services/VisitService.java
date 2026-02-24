/**
 * Сервисный слой для работы с визитами животных.
 *
 * <p>
 * Класс инкапсулирует бизнес‑логику, связанную с сущностью {@link Visit},
 * и обеспечивает взаимодействие с репозиторием {@link VisitRepository}.
 * Сервис используется контроллером VisitController для выполнения операций
 * чтения, сохранения, удаления и проверки корректности визитов.
 * </p>
 *
 * <p><b>Назначение сервиса:</b></p>
 * <ul>
 *     <li>Получение списка всех визитов</li>
 *     <li>Поиск визита по идентификатору</li>
 *     <li>Сохранение нового или обновлённого визита</li>
 *     <li>Удаление визита</li>
 *     <li>Проверка пересечения визитов по времени</li>
 *     <li>Подсчёт количества визитов на выбранную дату</li>
 * </ul>
 *
 * <p><b>Особенности реализации:</b></p>
 * <ul>
 *     <li>Визит считается длительностью 1 час</li>
 *     <li>Метод {@code hasOverlappingVisit()} вычисляет временной интервал
 *         и передаёт его в репозиторий для проверки пересечения</li>
 *     <li>Метод {@code countByDate()} используется для ограничения
 *         количества визитов в день (например, максимум 8)</li>
 * </ul>
 *
 * <p><b>Используется в случаях:</b></p>
 * <ul>
 *     <li>При создании нового визита</li>
 *     <li>При редактировании существующего визита</li>
 *     <li>При проверке доступности времени</li>
 *     <li>При удалении визита</li>
 * </ul>
 */

package com.hfbe_Polina.web_project.services;
import com.hfbe_Polina.web_project.entities.Visit;
import com.hfbe_Polina.web_project.repositories.VisitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Сервисный слой для работы с визитами животных.
 */
@Service
public class VisitService {

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public List<Visit> findAll() {
        return visitRepository.findAll();
    }

    public Visit findById(Long id) {
        return visitRepository.findById(id).orElse(null);
    }

    public void save(Visit visit) {
        visitRepository.save(visit);
    }

    public void delete(Long id) {
        visitRepository.deleteById(id);
    }

    public boolean hasOverlappingVisit(LocalDate date, LocalTime time) {
        LocalTime newStart = time;
        LocalTime newEnd = time.plusHours(1);
        return visitRepository.hasOverlappingVisit(date, newStart, newEnd);
    }


    public int countByDate(LocalDate date) {
        return visitRepository.countByDate(date);
    }
}

