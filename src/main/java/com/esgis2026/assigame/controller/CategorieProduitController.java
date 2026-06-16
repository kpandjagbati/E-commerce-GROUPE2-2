package com.esgis2026.assigame.controller;

import com.esgis2026.assigame.entity.CategorieProduit;
import com.esgis2026.assigame.service.CategorieProduitService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorieproduit")
public class CategorieProduitController {
    private final CategorieProduitService categorieProduitService;

    public CategorieProduitController(CategorieProduitService categorieProduitService) {
        this.categorieProduitService = categorieProduitService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    public List<CategorieProduit> getAllCategorieProduits() {
        return categorieProduitService.getAllCategorieProduits();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public CategorieProduit addCategorieProduit(@RequestBody CategorieProduit categorieProduit) {
        return categorieProduitService.createCategorieProduit(categorieProduit);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategorieProduit(@PathVariable Long id) {
        categorieProduitService.deleteCategorieProduit(id);
    }

    @PutMapping("update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategorieProduit updateCategorieProduit(@RequestBody CategorieProduit categorieProduit, @PathVariable Long id) {
        return categorieProduitService.updateCategorieProduit(id, categorieProduit);
    }
}
