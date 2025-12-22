package com.hfbe_Polina.web_project.controllers;

import com.hfbe_Polina.web_project.entities.Owner;
import com.hfbe_Polina.web_project.services.OwnerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер, управляющий CRUD‑операциями над владельцами животных.
 * <p>
 * Обеспечивает отображение списка владельцев, создание новых записей,
 * редактирование существующих и удаление. Использует сервисный слой
 * {@link OwnerService} для выполнения бизнес‑логики и взаимодействия
 * с базой данных.
 * </p>
 *
 * <p><b>Основные функции контроллера:</b></p>
 * <ul>
 *     <li>Отображение списка владельцев</li>
 *     <li>Добавление нового владельца</li>
 *     <li>Редактирование данных владельца</li>
 *     <li>Удаление владельца</li>
 * </ul>
 *
 * <p><b>Связанные сущности:</b></p>
 * <ul>
 *     <li>{@link com.hfbe_Polina.web_project.entities.Owner}</li>
 *     <li>{@link com.hfbe_Polina.web_project.entities.Pet}</li>
 * </ul>
 */
@Controller
@RequestMapping("/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * Отображает список всех владельцев.
     *
     * @param model модель представления, содержащая коллекцию владельцев
     * @return шаблон owners/list.html
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("owners", ownerService.findAll());
        return "owners/list";
    }

    /**
     * Отображает форму добавления нового владельца.
     *
     * @param model модель представления
     * @return шаблон owners/add.html
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("owner", new Owner());
        return "owners/add";
    }

    /**
     * Обрабатывает отправку формы добавления владельца.
     *
     * @param owner объект владельца, прошедший валидацию
     * @param result результат проверки данных
     * @return перенаправление на список владельцев или возврат формы при ошибках
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("owner") Owner owner,
                      BindingResult result) {

        if (result.hasErrors()) {
            return "owners/add";
        }

        ownerService.save(owner);
        return "redirect:/owners";
    }

    /**
     * Отображает форму редактирования владельца.
     *
     * @param id идентификатор владельца
     * @param model модель представления
     * @return шаблон owners/edit.html
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("owner", ownerService.findById(id));
        return "owners/edit";
    }

    /**
     * Обрабатывает отправку формы редактирования владельца.
     *
     * @param id идентификатор владельца
     * @param owner обновлённые данные владельца
     * @param result результат валидации
     * @return перенаправление или возврат формы при ошибках
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("owner") Owner owner,
                       BindingResult result) {

        if (result.hasErrors()) {
            owner.setId(id);
            return "owners/edit";
        }

        owner.setId(id);
        ownerService.save(owner);
        return "redirect:/owners";
    }

    /**
     * Удаляет владельца по идентификатору.
     *
     * @param id идентификатор владельца
     * @return перенаправление на список владельцев
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        ownerService.delete(id);
        return "redirect:/owners";
    }
}
