package com.hfbe_Polina.web_project.controllers;

import com.hfbe_Polina.web_project.entities.Visit;
import com.hfbe_Polina.web_project.services.PetService;
import com.hfbe_Polina.web_project.services.VisitService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
/**
 * Контроллер, управляющий CRUD‑операциями над визитами животных.
 *
 * <p>
 * Обрабатывает маршруты, связанные с созданием, редактированием, просмотром и удалением визитов.
 * Контроллер реализует бизнес‑правила расписания, включая:
 * </p>
 *
 * <ul>
 *     <li>ограничение даты визита (не позже 12 марта 2026 года);</li>
 *     <li>ограничение времени визита (с 08:00 до 15:00);</li>
 *     <li>проверку пересечения визитов (визит длится 1 час);</li>
 *     <li>лимит — не более 8 визитов в день;</li>
 *     <li>запрет переноса визита на более раннюю дату;</li>
 *     <li>корректную обработку ошибок валидации и повторный вывод формы.</li>
 * </ul>
 *
 * <p>
 * Контроллер взаимодействует с сервисами {@link VisitService} и {@link PetService}
 * для получения данных о визитах и животных, а также для выполнения проверок.
 * </p>
 *
 * <p><b>Основные маршруты:</b></p>
 * <ul>
 *     <li><b>GET /visits</b> — отображение списка всех визитов</li>
 *     <li><b>GET /visits/add</b> — форма создания визита</li>
 *     <li><b>POST /visits/add</b> — обработка создания визита</li>
 *     <li><b>GET /visits/edit/{id}</b> — форма редактирования визита</li>
 *     <li><b>POST /visits/edit/{id}</b> — обработка редактирования визита</li>
 *     <li><b>GET /visits/delete/{id}</b> — удаление визита</li>
 * </ul>
 *
 * <p><b>Особенности реализации:</b></p>
 * <ul>
 *     <li>При ошибках валидации форма повторно отображается с сохранением введённых данных.</li>
 *     <li>При редактировании визита поля diagnosis, treatment и pet недоступны для изменения.</li>
 *     <li>Проверка пересечения визитов выполняется через сервисный слой.</li>
 *     <li>Используются как стандартные ошибки BindingResult, так и кастомные ошибки (dateError, timeError, limitError).</li>
 * </ul>
 *
 * <p><b>Используется в случаях:</b></p>
 * <ul>
 *     <li>Когда пользователь создаёт новый визит</li>
 *     <li>Когда требуется изменить дату или время существующего визита</li>
 *     <li>Когда необходимо проверить доступность времени</li>
 *     <li>Когда требуется удалить визит</li>
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

    @GetMapping
    public String list(Model model) {
        model.addAttribute("visits", visitService.findAll());
        return "visits/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("visit", new Visit());
        model.addAttribute("pets", petService.findAll());
        return "visits/add";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("visit") Visit visit,
                      BindingResult result,
                      Model model) {

        if (result.hasErrors()) {
            model.addAttribute("pets", petService.findAll());
            return "visits/add";
        }

        //  Дата не пришла
        if (visit.getDate() == null) {
            model.addAttribute("dateError", "Введите корректную дату визита");
            model.addAttribute("pets", petService.findAll());
            return "visits/add";
        }

        // Ограничение даты
        if (visit.getDate().isAfter(LocalDate.of(2026, 3, 12))) {
            model.addAttribute("dateError", "Дата визита должна быть до 12 марта 2026");
            model.addAttribute("pets", petService.findAll());
            return "visits/add";
        }

        // Ограничение времени
        if (visit.getTime() == null ||
                visit.getTime().isBefore(LocalTime.of(8, 0)) ||
                visit.getTime().isAfter(LocalTime.of(15, 0))) {

            model.addAttribute("timeError", "Визиты принимаются с 08:00 до 15:00");
            model.addAttribute("pets", petService.findAll());
            return "visits/add";
        }

        // Пересечение визитов
        if (visitService.hasOverlappingVisit(visit.getDate(), visit.getTime())) {
            model.addAttribute("timeError", "Это время пересекается с другим визитом");
            model.addAttribute("pets", petService.findAll());
            return "visits/add";
        }

        // Лимит 8 визитов
        if (visitService.countByDate(visit.getDate()) >= 8) {
            model.addAttribute("limitError", "На этот день уже записано 8 визитов");
            model.addAttribute("pets", petService.findAll());
            return "visits/add";
        }

        visitService.save(visit);
        return "redirect:/visits";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("visit", visitService.findById(id));
        model.addAttribute("pets", petService.findAll());
        return "visits/edit";
    }

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

        // ❗ Дата не пришла (браузер не отправил её)
        if (visit.getDate() == null) {
            model.addAttribute("dateError", "Введите корректную дату визита");
            model.addAttribute("pets", petService.findAll());
            return "visits/edit";
        }

        // Ограничение даты
        if (visit.getDate().isAfter(LocalDate.of(2026, 3, 12))) {
            model.addAttribute("dateError", "Дата визита должна быть до 12 марта 2026");
            model.addAttribute("pets", petService.findAll());
            return "visits/edit";
        }

        // Ограничение времени
        if (visit.getTime() == null ||
                visit.getTime().isBefore(LocalTime.of(8, 0)) ||
                visit.getTime().isAfter(LocalTime.of(15, 0))) {

            model.addAttribute("timeError", "Визиты принимаются с 08:00 до 15:00");
            model.addAttribute("pets", petService.findAll());
            return "visits/edit";
        }

        Visit existing = visitService.findById(id);

        // Запрет переноса на более раннюю дату
        if (visit.getDate().isBefore(existing.getDate())) {
            model.addAttribute("dateError", "Нельзя перенести визит на более раннюю дату");
            model.addAttribute("pets", petService.findAll());
            return "visits/edit";
        }

        boolean timeChanged = !existing.getTime().equals(visit.getTime())
                || !existing.getDate().equals(visit.getDate());

        // Пересечение визитов
        if (timeChanged && visitService.hasOverlappingVisit(visit.getDate(), visit.getTime())) {
            model.addAttribute("timeError", "Это время пересекается с другим визитом");
            model.addAttribute("pets", petService.findAll());
            return "visits/edit";
        }

        // Лимит 8 визитов
        if (!existing.getDate().equals(visit.getDate())
                && visitService.countByDate(visit.getDate()) >= 8) {

            model.addAttribute("limitError", "На этот день уже записано 8 визитов");
            model.addAttribute("pets", petService.findAll());
            return "visits/edit";
        }

        // Поля, которые нельзя менять
        visit.setId(id);
        visit.setPet(existing.getPet());
        visit.setDiagnosis(existing.getDiagnosis());
        visit.setTreatment(existing.getTreatment());

        visitService.save(visit);
        return "redirect:/visits";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        visitService.delete(id);
        return "redirect:/visits";
    }
}
