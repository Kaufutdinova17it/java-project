package com.hfbe_Polina.web_project.controllers;

import com.hfbe_Polina.web_project.entities.Visit;
import com.hfbe_Polina.web_project.services.PetService;
import com.hfbe_Polina.web_project.services.VisitService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер, управляющий CRUD‑операциями над визитами животных.
 * <p>
 * Обеспечивает отображение списка визитов, создание новых записей,
 * редактирование существующих и удаление. Также предоставляет выбор животного
 * при создании или редактировании визита.
 * </p>
 *
 * <p><b>Особенности:</b></p>
 * <ul>
 *     <li>Поддерживает валидацию данных визита, включая проверку диапазона дат</li>
 *     <li>Связан с сущностями {@link com.hfbe_Polina.web_project.entities.Visit} и {@link com.hfbe_Polina.web_project.entities.Pet}</li>
 *     <li>Использует сервисы {@link VisitService} и {@link PetService}</li>
 * </ul>
 */
@Controller
@RequestMapping("/visits")
public class VisitController {

    private final VisitService visitService;
    private final PetService petService;

    public VisitController(VisitService visitService, PetService petService) {
        this.visitService = visitService;
        this.petService = petService;
    }

    /**
     * Отображает список всех визитов.
     *
     * @param model модель представления
     * @return шаблон visits/list.html
     */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("visits", visitService.findAll());
        return "visits/list";
    }

    /**
     * Отображает форму добавления нового визита.
     *
     * @param model модель представления
     * @return шаблон visits/add.html
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("visit", new Visit());
        model.addAttribute("pets", petService.findAll());
        return "visits/add";
    }

    /**
     * Обрабатывает отправку формы добавления визита.
     *
     * @param visit объект визита, прошедший валидацию
     * @param result результат проверки данных
     * @param model модель представления
     * @return перенаправление или возврат формы при ошибках
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("visit") Visit visit,
                      BindingResult result,
                      Model model) {

        if (result.hasErrors()) {
            model.addAttribute("pets", petService.findAll());
            return "visits/add";
        }

        visitService.save(visit);
        return "redirect:/visits";
    }

    /**
     * Отображает форму редактирования визита.
     *
     * @param id идентификатор визита
     * @param model модель представления
     * @return шаблон visits/edit.html
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("visit", visitService.findById(id));
        model.addAttribute("pets", petService.findAll());
        return "visits/edit";
    }

    /**
     * Обрабатывает отправку формы редактирования визита.
     *
     * @param id идентификатор визита
     * @param visit обновлённые данные визита
     * @param result результат валидации
     * @param model модель представления
     * @return перенаправление или возврат формы при ошибках
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("visit") Visit visit,
                       BindingResult result,
                       Model model) {

        if (result.hasErrors()) {
            visit.setId(id);
            model.addAttribute("pets", petService.findAll());
            return "visits/edit";
        }

        visit.setId(id);
        visitService.save(visit);
        return "redirect:/visits";
    }

    /**
     * Удаляет визит по идентификатору.
     *
     * @param id идентификатор визита
     * @return перенаправление на список визитов
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        visitService.delete(id);
        return "redirect:/visits";
    }
}
