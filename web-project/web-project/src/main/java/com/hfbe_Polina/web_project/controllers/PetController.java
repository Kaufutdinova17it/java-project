package com.hfbe_Polina.web_project.controllers;

import com.hfbe_Polina.web_project.entities.Pet;
import com.hfbe_Polina.web_project.services.OwnerService;
import com.hfbe_Polina.web_project.services.PetService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
                      @RequestParam("owner.id") Long ownerId,
                      Model model) {

        if (result.hasErrors()) {
            model.addAttribute("owners", ownerService.findAll());
            return "pets/add";
        }

        // ВАЖНО: вручную устанавливаем владельца
        pet.setOwner(ownerService.findById(ownerId));

        petService.save(pet);
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
                       @RequestParam("owner.id") Long ownerId,
                       Model model) {

        if (result.hasErrors()) {
            pet.setId(id);
            model.addAttribute("owners", ownerService.findAll());
            return "pets/edit";
        }

        pet.setId(id);

        // ВАЖНО: вручную устанавливаем владельца
        pet.setOwner(ownerService.findById(ownerId));

        petService.save(pet);
        return "redirect:/pets";
    }

    // ============================
    // Удаление
    // ============================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        petService.delete(id);
        return "redirect:/pets";
    }
}
