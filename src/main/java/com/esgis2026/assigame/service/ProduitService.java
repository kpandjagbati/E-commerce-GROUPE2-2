package com.esgis2026.assigame.service;

import com.esgis2026.assigame.entity.Produit;
import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.repository.ProduitRepository;
import com.esgis2026.assigame.security.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProduitService {

    public static final String STATUT_EN_ATTENTE = "EN_ATTENTE";
    public static final String STATUT_ACTIF = "ACTIF";
    public static final String STATUT_REFUSE = "REFUSE";
    public static final String STATUT_INACTIF = "INACTIF";
    public static final String STATUT_VENDU = "VENDU";

    final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() {
        if (SecurityUtils.isAdmin()) {
            return produitRepository.findAll();
        }
        return getMesProduits();
    }

    public List<Produit> getMesProduits() {
        Long currentUserId = SecurityUtils.getCurrentUtilisateur().getId_utilisateur();
        return produitRepository.findByVendeurId(currentUserId);
    }

    public Produit createProduit(Produit produit, MultipartFile image) throws IOException {
        Utilisateur currentUser = SecurityUtils.getCurrentUtilisateur();

        produit.setImage_type(image.getContentType());
        produit.setImage(image.getBytes());
        produit.setDate_ajout(LocalDateTime.now());
        produit.setUtilisateur(currentUser);

        if (SecurityUtils.isVendeur()) {
            produit.setStatut(STATUT_EN_ATTENTE);
        } else if (produit.getStatut() == null || produit.getStatut().isBlank()) {
            produit.setStatut(STATUT_ACTIF);
        }

        return produitRepository.save(produit);
    }

    public Produit updateProduit(Produit details, Long id) {
        Produit produit = getProduitById(id);
        ensureCanModifyProduit(produit);

        if (details.getNom_produit() != null) {
            produit.setNom_produit(details.getNom_produit());
        }
        if (details.getPrix() != null) {
            produit.setPrix(details.getPrix());
        }
        if (details.getDescription() != null) {
            produit.setDescription(details.getDescription());
        }
        if (details.getImage() != null) {
            produit.setImage(details.getImage());
        }
        if (details.getImage_type() != null) {
            produit.setImage_type(details.getImage_type());
        }
        if (details.getCategorie_produit() != null) {
            produit.setCategorie_produit(details.getCategorie_produit());
        }
        if (details.getDate_ajout() != null) {
            produit.setDate_ajout(details.getDate_ajout());
        }

        if (details.getStatut() != null) {
            applyStatutUpdate(produit, details.getStatut());
        } else if (SecurityUtils.isVendeur()) {
            produit.setStatut(STATUT_EN_ATTENTE);
        }

        return produitRepository.save(produit);
    }

    public Produit updateStatut(Long id, String statut) {
        if (!SecurityUtils.isAdmin()) {
            throw new AccessDeniedException("Seul un administrateur peut valider un produit");
        }

        Produit produit = getProduitById(id);
        produit.setStatut(statut);
        return produitRepository.save(produit);
    }

    public void deleteProduit(Long id) {
        Produit produit = getProduitById(id);
        ensureCanModifyProduit(produit);
        produitRepository.deleteById(id);
    }

    public Produit getProduitById(Long id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("produit not found with id " + id));
        ensureCanViewProduit(produit);
        return produit;
    }

    public Produit uploadImage(Long id, MultipartFile file) throws IOException {
        Produit produit = getProduitById(id);
        ensureCanModifyProduit(produit);
        produit.setImage(file.getBytes());
        produit.setImage_type(file.getContentType());
        return produitRepository.save(produit);
    }

    private void applyStatutUpdate(Produit produit, String statut) {
        if (SecurityUtils.isAdmin()) {
            produit.setStatut(statut);
            return;
        }

        if (STATUT_INACTIF.equals(statut) || STATUT_VENDU.equals(statut)) {
            produit.setStatut(statut);
            return;
        }

        throw new AccessDeniedException("Le vendeur ne peut pas definir ce statut");
    }

    private void ensureCanViewProduit(Produit produit) {
        if (SecurityUtils.isAdmin()) {
            return;
        }

        Long ownerId = produit.getUtilisateur().getId_utilisateur();
        if (!SecurityUtils.getCurrentUtilisateur().getId_utilisateur().equals(ownerId)) {
            throw new AccessDeniedException("Acces refuse a ce produit");
        }
    }

    private void ensureCanModifyProduit(Produit produit) {
        ensureCanViewProduit(produit);
    }
}
