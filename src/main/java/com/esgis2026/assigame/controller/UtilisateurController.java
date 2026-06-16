package com.esgis2026.assigame.controller;

import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.service.UtilisateurService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateur")
public class UtilisateurController {

    final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Utilisateur> getAllUtilisateur() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    public Utilisateur getCurrentUtilisateur() {
        return utilisateurService.getCurrentUtilisateur();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public Utilisateur createUtilisateur(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.createUtilisateur(utilisateur);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUtilisateur(@PathVariable Long id) {
        utilisateurService.deleteUtilisateur(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Utilisateur updateUtilisateur(@RequestBody Utilisateur utilisateur, @PathVariable Long id) {
        return utilisateurService.updateUtilisateur(utilisateur, id);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    public Utilisateur updateCurrentProfile(@RequestBody Utilisateur utilisateur) {
        return utilisateurService.updateCurrentProfile(utilisateur);
    }
}
