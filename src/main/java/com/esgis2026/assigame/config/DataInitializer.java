package com.esgis2026.assigame.config;

import com.esgis2026.assigame.entity.TypeUtilisateur;
import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.repository.ProduitRepository;
import com.esgis2026.assigame.repository.TypeUtilisateurRepository;
import com.esgis2026.assigame.repository.UtilisateurRepository;
import com.esgis2026.assigame.service.ProduitService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TypeUtilisateurRepository typeUtilisateurRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ProduitRepository produitRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            TypeUtilisateurRepository typeUtilisateurRepository,
            UtilisateurRepository utilisateurRepository,
            ProduitRepository produitRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.typeUtilisateurRepository = typeUtilisateurRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.produitRepository = produitRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        TypeUtilisateur adminType = createTypeIfMissing("ADMIN", "Administrateur de la plateforme");
        TypeUtilisateur vendeurType = createTypeIfMissing("VENDEUR", "Vendeur de produits");

        if (utilisateurRepository.findByLogin_utilisateur("admin").isEmpty()) {
            Utilisateur admin = new Utilisateur();
            admin.setNom_utilisateur("Admin");
            admin.setPrenom_utilisateur("Systeme");
            admin.setSexe_utilisateur("M");
            admin.setTelephone_urilisateur("0000000000");
            admin.setMail_utilisateur("admin@assigame.com");
            admin.setLogin_utilisateur("admin");
            admin.setPassword_utilisateur(passwordEncoder.encode("admin123"));
            admin.setResidence_utilisateur("Lome");
            admin.setType_utilisateur(adminType);
            utilisateurRepository.save(admin);
        }

        if (utilisateurRepository.findByLogin_utilisateur("vendeur").isEmpty()) {
            Utilisateur vendeur = new Utilisateur();
            vendeur.setNom_utilisateur("Vendeur");
            vendeur.setPrenom_utilisateur("Test");
            vendeur.setSexe_utilisateur("M");
            vendeur.setTelephone_urilisateur("1111111111");
            vendeur.setMail_utilisateur("vendeur@assigame.com");
            vendeur.setLogin_utilisateur("vendeur");
            vendeur.setPassword_utilisateur(passwordEncoder.encode("vendeur123"));
            vendeur.setResidence_utilisateur("Lome");
            vendeur.setType_utilisateur(vendeurType);
            utilisateurRepository.save(vendeur);
        }

        produitRepository.findAll().stream()
                .filter(p -> ProduitService.STATUT_EN_ATTENTE.equals(p.getStatut()))
                .forEach(p -> {
                    p.setStatut(ProduitService.STATUT_ACTIF);
                    produitRepository.save(p);
                });
    }

    private TypeUtilisateur createTypeIfMissing(String libelle, String description) {
        return typeUtilisateurRepository.findByLibelle(libelle)
                .orElseGet(() -> {
                    TypeUtilisateur type = new TypeUtilisateur();
                    type.setLibelle_type_utilisateur(libelle);
                    type.setDescription_type_utilisateur(description);
                    return typeUtilisateurRepository.save(type);
                });
    }
}
