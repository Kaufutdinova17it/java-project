package com.hfbe_Polina.web_project.services;

import com.hfbe_Polina.web_project.entities.Visit;
import com.hfbe_Polina.web_project.repositories.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервисный слой для работы с визитами животных.
 * <p>
 * Инкапсулирует бизнес‑логику, связанную с сущностью {@link Visit},
 * и обеспечивает взаимодействие с репозиторием {@link VisitRepository}.
 * </p>
 *
 * <p><b>Основные задачи сервиса:</b></p>
 * <ul>
 *     <li>Получение списка визитов</li>
 *     <li>Поиск визита по идентификатору</li>
 *     <li>Сохранение нового или обновлённого визита</li>
 *     <li>Удаление визита</li>
 * </ul>
 *
 * <p><b>Особенности:</b></p>
 * <ul>
 *     <li>Поддерживает валидацию дат визита</li>
 *     <li>Используется контроллером VisitController</li>
 * </ul>
 */
@Service
public class VisitService {

    private final VisitRepository visitRepository;

    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    /**
     * Возвращает список всех визитов.
     *
     * @return список визитов
     */
    public List<Visit> findAll() {
        return visitRepository.findAll();
    }

    /**
     * Ищет визит по идентификатору.
     *
     * @param id идентификатор визита
     * @return найденный визит или null, если не найден
     */
    public Visit findById(Long id) {
        return visitRepository.findById(id).orElse(null);
    }

    /**
     * Сохраняет новый или обновлённый визит.
     *
     * @param visit объект визита
     */
    public void save(Visit visit) {
        visitRepository.save(visit);
    }

    /**
     * Удаляет визит по идентификатору.
     *
     * @param id идентификатор визита
     */
    public void delete(Long id) {
        visitRepository.deleteById(id);
    }
}
