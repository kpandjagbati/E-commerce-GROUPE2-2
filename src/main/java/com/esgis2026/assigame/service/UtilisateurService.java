package com.esgis2026.assigame.service;

import com.esgis2026.assigame.entity.TypeUtilisateur;
import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.repository.UtilisateurRepository;
import com.esgis2026.assigame.repository.TypeUtilisateurRepository;
import com.esgis2026.assigame.dto.RegisterVendeurRequest;
import com.esgis2026.assigame.security.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
    final UtilisateurRepository utilisateurRepository;
    private final TypeUtilisateurRepository typeUtilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurService(
            UtilisateurRepository utilisateurRepository,
            TypeUtilisateurRepository typeUtilisateurRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.utilisateurRepository = utilisateurRepository;
        this.typeUtilisateurRepository = typeUtilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAllWithType();
    }

    public List<Utilisateur> getVendeurs() {
        return utilisateurRepository.findAllByRole("VENDEUR");
    }

    public Utilisateur getCurrentUtilisateur() {
        return utilisateurRepository.findById(SecurityUtils.getCurrentUtilisateur().getId_utilisateur())
                .orElseThrow(() -> new RuntimeException("utilisateur n'existe pas"));
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        utilisateur.setPassword_utilisateur(passwordEncoder.encode(utilisateur.getPassword_utilisateur()));
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur registerVendeur(RegisterVendeurRequest request) {
        if (request.getNom() == null || request.getNom().isBlank()) {
            throw new RuntimeException("Le nom est obligatoire");
        }
        if (request.getPrenom() == null || request.getPrenom().isBlank()) {
            throw new RuntimeException("Le prénom est obligatoire");
        }
        if (request.getLogin() == null || request.getLogin().isBlank()) {
            throw new RuntimeException("Le nom d'utilisateur est obligatoire");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new RuntimeException("Le mot de passe doit contenir au moins 6 caractères");
        }
        if (request.getTelephone() == null || request.getTelephone().isBlank()) {
            throw new RuntimeException("Le numéro de téléphone est obligatoire");
        }

        String login = request.getLogin().trim();
        String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : null;
        String telephone = request.getTelephone().trim();

        if (utilisateurRepository.findByLogin_utilisateur(login).isPresent()) {
            throw new RuntimeException("Ce nom d'utilisateur est déjà utilisé");
        }
        if (email != null && !email.isBlank()
                && utilisateurRepository.findByMail_utilisateur(email).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        if (utilisateurRepository.findByTelephone_urilisateur(telephone).isPresent()) {
            throw new RuntimeException("Ce numéro de téléphone est déjà utilisé");
        }

        TypeUtilisateur vendeurType = typeUtilisateurRepository.findByLibelle("VENDEUR")
                .orElseThrow(() -> new RuntimeException("Le type vendeur n'est pas configuré"));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom_utilisateur(request.getNom().trim());
        utilisateur.setPrenom_utilisateur(request.getPrenom().trim());
        utilisateur.setLogin_utilisateur(login);
        utilisateur.setMail_utilisateur(email);
        utilisateur.setTelephone_urilisateur(telephone);
        utilisateur.setPassword_utilisateur(passwordEncoder.encode(request.getPassword()));
        utilisateur.setSexe_utilisateur(
                request.getSexe() != null && !request.getSexe().isBlank() ? request.getSexe().trim() : "M"
        );
        utilisateur.setResidence_utilisateur(
                request.getResidence() != null ? request.getResidence().trim() : null
        );
        utilisateur.setType_utilisateur(vendeurType);

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
