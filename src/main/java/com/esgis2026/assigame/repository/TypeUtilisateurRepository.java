package com.esgis2026.assigame.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esgis2026.assigame.entity.TypeUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeUtilisateurRepository extends JpaRepository<TypeUtilisateur, Long> {

    @Query("SELECT t FROM TypeUtilisateur t WHERE UPPER(t.libelle_type_utilisateur) = UPPER(:libelle)")
    Optional<TypeUtilisateur> findByLibelle(@Param("libelle") String libelle);
}
