package com.hfbe_Polina.web_project.services;

import com.hfbe_Polina.web_project.entities.Owner;
import com.hfbe_Polina.web_project.repositories.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервисный слой для работы с владельцами животных.
 * <p>
 * Инкапсулирует бизнес‑логику, связанную с сущностью {@link Owner},
 * и обеспечивает взаимодействие с репозиторием {@link OwnerRepository}.
 * </p>
 *
 * <p><b>Основные задачи сервиса:</b></p>
 * <ul>
 *     <li>Получение списка владельцев</li>
 *     <li>Поиск владельца по идентификатору</li>
 *     <li>Сохранение нового или обновлённого владельца</li>
 *     <li>Удаление владельца</li>
 * </ul>
 */
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public boolean existsByEmail(String email) {
        return ownerRepository.existsByEmail(email);
    }
    public boolean existsByPhone(String phone) {
        return ownerRepository.existsByPhone(phone);
    }



    /**
     * Возвращает список всех владельцев.
     *
     * @return список владельцев
     */
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }

    /**
     * Ищет владельца по идентификатору.
     *
     * @param id идентификатор владельца
     * @return найденный владелец
     */
    public Owner findById(Long id) {
        return ownerRepository.findById(id).orElse(null);
    }

    /**
     * Сохраняет нового или обновлённого владельца.
     *
     * @param owner объект владельца
     */
    public void save(Owner owner) {
        ownerRepository.save(owner);
    }

    /**
     * Удаляет владельца по идентификатору.
     *
     * @param id идентификатор владельца
     */
    public void delete(Long id) {
        ownerRepository.deleteById(id);
    }
}
