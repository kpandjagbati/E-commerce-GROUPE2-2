package com.esgis2026.assigame.service;

import com.esgis2026.assigame.dto.DashboardStatsDto;
import com.esgis2026.assigame.dto.ProduitResumeDto;
import com.esgis2026.assigame.dto.UtilisateurResumeDto;
import com.esgis2026.assigame.dto.VendeurDashboardStatsDto;
import com.esgis2026.assigame.entity.Produit;
import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.repository.ProduitRepository;
import com.esgis2026.assigame.repository.UtilisateurRepository;
import com.esgis2026.assigame.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final ProduitRepository produitRepository;
    private final UtilisateurRepository utilisateurRepository;

    public DashboardService(ProduitRepository produitRepository, UtilisateurRepository utilisateurRepository) {
        this.produitRepository = produitRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public DashboardStatsDto getAdminStats() {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAllWithType();
        List<Produit> produits = produitRepository.findAll();

        long totalVendeurs = utilisateurs.stream()
                .filter(u -> u.getType_utilisateur() != null
                        && "VENDEUR".equals(u.getType_utilisateur().getLibelle_type_utilisateur()))
                .count();

        Map<String, Long> produitsParStatut = countByStatut(produits);
        long enAttente = produitsParStatut.getOrDefault(ProduitService.STATUT_EN_ATTENTE, 0L);
        long actifs = produitsParStatut.getOrDefault(ProduitService.STATUT_ACTIF, 0L);

        double chiffreAffaires = produits.stream()
                .filter(p -> ProduitService.STATUT_VENDU.equals(p.getStatut()))
                .mapToDouble(p -> p.getPrix() != null ? p.getPrix() : 0)
                .sum();

        List<ProduitResumeDto> recents = produits.stream()
                .sorted(Comparator.comparing(Produit::getDate_ajout, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .map(this::toProduitResume)
                .toList();

        List<UtilisateurResumeDto> vendeursRecents = utilisateurs.stream()
                .filter(u -> u.getType_utilisateur() != null
                        && "VENDEUR".equals(u.getType_utilisateur().getLibelle_type_utilisateur()))
                .sorted(Comparator.comparing(Utilisateur::getId_utilisateur).reversed())
                .limit(5)
                .map(this::toUtilisateurResume)
                .toList();

        return new DashboardStatsDto(
                utilisateurs.size(),
                totalVendeurs,
                produits.size(),
                enAttente,
                actifs,
                chiffreAffaires,
                produitsParStatut,
                recents,
                vendeursRecents
        );
    }

    public VendeurDashboardStatsDto getVendeurStats() {
        Long vendeurId = SecurityUtils.getCurrentUtilisateur().getId_utilisateur();
        List<Produit> produits = produitRepository.findByVendeurId(vendeurId);

        Map<String, Long> produitsParStatut = countByStatut(produits);
        long enAttente = produitsParStatut.getOrDefault(ProduitService.STATUT_EN_ATTENTE, 0L);
        long actifs = produitsParStatut.getOrDefault(ProduitService.STATUT_ACTIF, 0L);

        double totalVentes = produits.stream()
                .filter(p -> ProduitService.STATUT_VENDU.equals(p.getStatut()))
                .mapToDouble(p -> p.getPrix() != null ? p.getPrix() : 0)
                .sum();

        List<ProduitResumeDto> recents = produits.stream()
                .sorted(Comparator.comparing(Produit::getDate_ajout, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .map(this::toProduitResume)
                .toList();

        return new VendeurDashboardStatsDto(
                produits.size(),
                enAttente,
                actifs,
                totalVentes,
                produitsParStatut,
                recents
        );
    }

    private Map<String, Long> countByStatut(List<Produit> produits) {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (Produit produit : produits) {
            String statut = produit.getStatut() != null ? produit.getStatut() : "INCONNU";
            counts.merge(statut, 1L, Long::sum);
        }
        return counts;
    }

    private ProduitResumeDto toProduitResume(Produit produit) {
        String categorie = produit.getCategorie_produit() != null
                ? produit.getCategorie_produit().getNom_categorieproduit()
                : null;
        String vendeur = produit.getUtilisateur() != null
                ? produit.getUtilisateur().getPrenom_utilisateur() + " " + produit.getUtilisateur().getNom_utilisateur()
                : null;

        return new ProduitResumeDto(
                produit.getId_produit(),
                produit.getNom_produit(),
                produit.getPrix(),
                produit.getStatut(),
                categorie,
                vendeur,
                produit.getDate_ajout(),
                produit.getImage() != null && produit.getImage().length > 0
        );
    }

    private UtilisateurResumeDto toUtilisateurResume(Utilisateur utilisateur) {
        String role = utilisateur.getType_utilisateur() != null
                ? utilisateur.getType_utilisateur().getLibelle_type_utilisateur()
                : null;

        return new UtilisateurResumeDto(
                utilisateur.getId_utilisateur(),
                utilisateur.getNom_utilisateur(),
                utilisateur.getPrenom_utilisateur(),
                utilisateur.getMail_utilisateur(),
                utilisateur.getLogin_utilisateur(),
                role,
                utilisateur.getTelephone_urilisateur()
        );
    }
}
