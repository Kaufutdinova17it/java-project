package com.hfbe_Polina.web_project.services;

import com.hfbe_Polina.web_project.entities.Pet;
import com.hfbe_Polina.web_project.repositories.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервисный слой для работы с животными.
 * <p>
 * Инкапсулирует бизнес‑логику, связанную с сущностью {@link Pet},
 * и обеспечивает взаимодействие с репозиторием {@link PetRepository}.
 * </p>
 *
 * <p><b>Основные задачи сервиса:</b></p>
 * <ul>
 *     <li>Получение списка животных</li>
 *     <li>Поиск животного по идентификатору</li>
 *     <li>Сохранение нового или обновлённого животного</li>
 *     <li>Удаление животного</li>
 * </ul>
 */
@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Возвращает список всех животных.
     *
     * @return список животных
     */
    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    /**
     * Ищет животное по идентификатору.
     *
     * @param id идентификатор животного
     * @return найденное животное
     */
    public Pet findById(Long id) {
        return petRepository.findById(id).orElse(null);
    }

    /**
     * Сохраняет новое или обновлённое животное.
     *
     * @param pet объект животного
     */
    public void save(Pet pet) {
        petRepository.save(pet);
    }

    /**
     * Удаляет животное по идентификатору.
     *
     * @param id идентификатор животного
     */
    public void delete(Long id) {
        petRepository.deleteById(id);
    }
}
