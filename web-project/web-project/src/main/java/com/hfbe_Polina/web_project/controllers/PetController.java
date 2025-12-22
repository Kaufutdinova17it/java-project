package com.hfbe_Polina.web_project.controllers;

import com.hfbe_Polina.web_project.entities.Pet;
import com.hfbe_Polina.web_project.services.OwnerService;
import com.hfbe_Polina.web_project.services.PetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер, управляющий CRUD‑операциями над животными.
 * <p>
 * Обеспечивает отображение списка животных, создание новых записей,
 * редактирование существующих и удаление. Также обеспечивает выбор владельца
 * при создании или редактировании животного.
 * </p>
 *
 * <p><b>Основные функции контроллера:</b></p>
 * <ul>
 *     <li>Отображение списка животных</li>
 *     <li>Добавление нового животного</li>
 *     <li>Редактирование данных животного</li>
 *     <li>Удаление животного</li>
 * </ul>
 *
 * <p><b>Связанные сущности:</b></p>
 * <ul>
 *     <li>{@link com.hfbe_Polina.web_project.entities.Pet}</li>
 *     <li>{@link com.hfbe_Polina.web_project.entities.Owner}</li>
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

    /**
     * Отображает список всех животных.
     *
     * @param model модель представления
     * @return шаблон pets/list.html
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("pets", petService.findAll());
        return "pets/list";
    }

    /**
     * Отображает форму добавления нового животного.
     *
     * @param model модель представления
     * @return шаблон pets/add.html
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("pet", new Pet());
        model.addAttribute("owners", ownerService.findAll());
        return "pets/add";
    }

    /**
     * Обрабатывает отправку формы добавления животного.
     *
     * @param pet объект животного, прошедший валидацию
     * @param result результат проверки данных
     * @param model модель представления
     * @return перенаправление или возврат формы при ошибках
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("pet") Pet pet,
                      BindingResult result,
                      Model model) {

        if (result.hasErrors()) {
            model.addAttribute("owners", ownerService.findAll());
            return "pets/add";
        }

        petService.save(pet);
        return "redirect:/pets";
    }

    /**
     * Отображает форму редактирования животного.
     *
     * @param id идентификатор животного
     * @param model модель представления
     * @return шаблон pets/edit.html
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("pet", petService.findById(id));
        model.addAttribute("owners", ownerService.findAll());
        return "pets/edit";
    }

    /**
     * Обрабатывает отправку формы редактирования животного.
     *
     * @param id идентификатор животного
     * @param pet обновлённые данные животного
     * @param result результат валидации
     * @param model модель представления
     * @return перенаправление или возврат формы при ошибках
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("pet") Pet pet,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors()) {
            pet.setId(id);
            model.addAttribute("owners", ownerService.findAll());
            return "pets/edit";
        }

        pet.setId(id);
        petService.save(pet);
        return "redirect:/pets";
    }

    /**
     * Удаляет животное по идентификатору.
     *
     * @param id идентификатор животного
     * @return перенаправление на список животных
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        petService.delete(id);
        return "redirect:/pets";
    }
}
