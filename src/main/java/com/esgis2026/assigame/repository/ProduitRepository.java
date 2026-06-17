package com.esgis2026.assigame.repository;

import com.esgis2026.assigame.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    @Query("SELECT p FROM Produit p WHERE p.utilisateur.id_utilisateur = :idUtilisateur")
    List<Produit> findByVendeurId(@Param("idUtilisateur") Long idUtilisateur);

    @Query("SELECT p FROM Produit p WHERE p.statut = :statut ORDER BY p.date_ajout DESC")
    List<Produit> findByStatut(@Param("statut") String statut);

    @Query("SELECT p FROM Produit p WHERE p.id_produit = :id AND p.statut = :statut")
    Optional<Produit> findCatalogueProduit(@Param("id") Long id, @Param("statut") String statut);
}
