package com.esgis2026.assigame.service;

import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.repository.UtilisateurRepository;
import com.esgis2026.assigame.security.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
    final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur getCurrentUtilisateur() {
        return utilisateurRepository.findById(SecurityUtils.getCurrentUtilisateur().getId_utilisateur())
                .orElseThrow(() -> new RuntimeException("utilisateur n'existe pas"));
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        utilisateur.setPassword_utilisateur(passwordEncoder.encode(utilisateur.getPassword_utilisateur()));
        return utilisateurRepository.save(utilisateur);
    }

    public void deleteUtilisateur(Long idUtilisateur) {
        utilisateurRepository.deleteById(idUtilisateur);
    }

    public Utilisateur updateUtilisateur(Utilisateur details, Long idUtilisateur) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("utilisateur n'existe pas"));

        utilisateur.setNom_utilisateur(details.getNom_utilisateur());
        utilisateur.setMail_utilisateur(details.getMail_utilisateur());
        utilisateur.setSexe_utilisateur(details.getSexe_utilisateur());
        utilisateur.setResidence_utilisateur(details.getResidence_utilisateur());
        utilisateur.setPrenom_utilisateur(details.getPrenom_utilisateur());
        utilisateur.setLogin_utilisateur(details.getLogin_utilisateur());
        utilisateur.setTelephone_urilisateur(details.getTelephone_urilisateur());

        if (details.getType_utilisateur() != null) {
            utilisateur.setType_utilisateur(details.getType_utilisateur());
        }

        if (details.getPassword_utilisateur() != null && !details.getPassword_utilisateur().isBlank()) {
            utilisateur.setPassword_utilisateur(passwordEncoder.encode(details.getPassword_utilisateur()));
        }

        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur updateCurrentProfile(Utilisateur details) {
        Long currentUserId = SecurityUtils.getCurrentUtilisateur().getId_utilisateur();
        Utilisateur utilisateur = utilisateurRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("utilisateur n'existe pas"));

        if (details.getNom_utilisateur() != null) {
            utilisateur.setNom_utilisateur(details.getNom_utilisateur());
        }
        if (details.getPrenom_utilisateur() != null) {
            utilisateur.setPrenom_utilisateur(details.getPrenom_utilisateur());
        }
        if (details.getMail_utilisateur() != null) {
            utilisateur.setMail_utilisateur(details.getMail_utilisateur());
        }
        if (details.getSexe_utilisateur() != null) {
            utilisateur.setSexe_utilisateur(details.getSexe_utilisateur());
        }
        if (details.getResidence_utilisateur() != null) {
            utilisateur.setResidence_utilisateur(details.getResidence_utilisateur());
        }
        if (details.getTelephone_urilisateur() != null) {
            utilisateur.setTelephone_urilisateur(details.getTelephone_urilisateur());
        }

        if (details.getPassword_utilisateur() != null && !details.getPassword_utilisateur().isBlank()) {
            utilisateur.setPassword_utilisateur(passwordEncoder.encode(details.getPassword_utilisateur()));
        }

        return utilisateurRepository.save(utilisateur);
    }

    public void ensureCanAccessUtilisateur(Long idUtilisateur) {
        if (SecurityUtils.isAdmin()) {
            return;
        }

        if (!SecurityUtils.getCurrentUtilisateur().getId_utilisateur().equals(idUtilisateur)) {
            throw new AccessDeniedException("Acces refuse a ce profil utilisateur");
        }
    }
}
