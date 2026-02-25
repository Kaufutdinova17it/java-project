package com.hfbe_Polina.web_project.controllers;

import com.hfbe_Polina.web_project.entities.Pet;
import com.hfbe_Polina.web_project.services.OwnerService;
import com.hfbe_Polina.web_project.services.PetService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер, управляющий CRUD‑операциями над животными.
 *
 * <p>
 * Обрабатывает маршруты, связанные с созданием, редактированием, просмотром и удалением
 * животных. Контроллер реализует бизнес‑правила, связанные с уникальностью паспортного
 * номера, неизменяемостью владельца при редактировании и невозможностью удаления животного,
 * у которого есть связанные визиты.
 * </p>
 *
 * <p><b>Основные функции контроллера:</b></p>
 * <ul>
 *     <li>Отображение списка всех животных</li>
 *     <li>Создание нового животного</li>
 *     <li>Редактирование существующего животного</li>
 *     <li>Удаление животного при отсутствии визитов</li>
 * </ul>
 *
 * <p><b>Бизнес‑правила, реализованные в контроллере:</b></p>
 * <ul>
 *     <li>Номер паспорта животного должен быть уникальным — нарушение приводит к
 *         <code>DataIntegrityViolationException</code> и отображению ошибки</li>
 *     <li>При редактировании животного владелец не может быть изменён</li>
 *     <li>Животное нельзя удалить, если у него есть хотя бы один визит</li>
 *     <li>При ошибках валидации форма повторно отображается с сохранением введённых данных</li>
 * </ul>
 *
 * <p><b>Основные маршруты:</b></p>
 * <ul>
 *     <li><b>GET /pets</b> — отображение списка животных</li>
 *     <li><b>GET /pets/add</b> — форма добавления животного</li>
 *     <li><b>POST /pets/add</b> — обработка создания животного</li>
 *     <li><b>GET /pets/edit/{id}</b> — форма редактирования животного</li>
 *     <li><b>POST /pets/edit/{id}</b> — обработка редактирования животного</li>
 *     <li><b>GET /pets/delete/{id}</b> — удаление животного</li>
 * </ul>
 *
 * <p><b>Особенности реализации:</b></p>
 * <ul>
 *     <li>Используются сервисы {@link PetService} и {@link OwnerService} для работы с данными</li>
 *     <li>Ошибки уникальности паспорта обрабатываются вручную через try/catch</li>
 *     <li>При редактировании данные владельца подставляются из существующей сущности</li>
 *     <li>При удалении выполняется проверка наличия визитов у животного</li>
 * </ul>
 *
 * <p><b>Используется в случаях:</b></p>
 * <ul>
 *     <li>Когда пользователь добавляет нового питомца</li>
 *     <li>Когда требуется изменить данные животного</li>
 *     <li>Когда необходимо предотвратить дублирование паспортных номеров</li>
 *     <li>Когда требуется безопасно удалить животное</li>
 * </ul>
 */


@Controller
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;
    private final OwnerService ownerService;

    public PetController(PetService petService, OwnerService ownerService) {
        this.petService = petService;
        this.ownerService = ownerService;
    }

    // ============================
    // Список животных
    // ============================
    @GetMapping
    public String list(Model model) {
        model.addAttribute("pets", petService.findAll());
        return "pets/list";
    }

    // ============================
    // Форма добавления
    // ============================
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("pet", new Pet());
        model.addAttribute("owners", ownerService.findAll());
        return "pets/add";
    }

    // ============================
    // Обработка добавления
    // ============================
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("pet") Pet pet,
                      BindingResult result,
                      Model model) {

        if (result.hasErrors()) {
            model.addAttribute("owners", ownerService.findAll());
            return "pets/add";
        }

        try {
            petService.save(pet);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("passportError", "Животное с таким номером паспорта уже существует");
            model.addAttribute("owners", ownerService.findAll());
            return "pets/add";
        }

        return "redirect:/pets";
    }

    // ============================
    // Форма редактирования
    // ============================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pet", petService.findById(id));
        model.addAttribute("owners", ownerService.findAll());
        return "pets/edit";
    }

    // ============================
    // Обработка редактирования
    // ============================
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("pet") Pet pet,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors()) {
            model.addAttribute("owners", ownerService.findAll());
            return "pets/edit";
        }

        Pet existing = petService.findById(id);

        // Владелец не меняется
        pet.setOwner(existing.getOwner());

        pet.setId(id);

        try {
            petService.save(pet);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("passportError", "Животное с таким номером паспорта уже существует");
            model.addAttribute("owners", ownerService.findAll());
            return "pets/edit";
        }

        return "redirect:/pets";
    }

    // ============================
    // Удаление
    // ============================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {

        Pet pet = petService.findById(id);

        // Нельзя удалить животное, если у него есть визиты
        if (!pet.getVisits().isEmpty()) {
            model.addAttribute("deleteError", "Нельзя удалить животное, у которого есть визиты");
            model.addAttribute("pets", petService.findAll());
            return "pets/list";
        }

        petService.delete(id);
        return "redirect:/pets";
    }
}
