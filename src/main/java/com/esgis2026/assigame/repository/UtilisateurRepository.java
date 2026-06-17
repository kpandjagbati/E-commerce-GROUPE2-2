package com.esgis2026.assigame.repository;

import com.esgis2026.assigame.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @Query("SELECT u FROM Utilisateur u JOIN FETCH u.type_utilisateur WHERE u.Login_utilisateur = :login")
    Optional<Utilisateur> findByLogin_utilisateur(@Param("login") String login);

    @Query("SELECT u FROM Utilisateur u WHERE u.mail_utilisateur = :email")
    Optional<Utilisateur> findByMail_utilisateur(@Param("email") String email);

    @Query("SELECT u FROM Utilisateur u WHERE u.telephone_urilisateur = :telephone")
    Optional<Utilisateur> findByTelephone_urilisateur(@Param("telephone") String telephone);

    @Query("SELECT u FROM Utilisateur u JOIN FETCH u.type_utilisateur")
    List<Utilisateur> findAllWithType();

    @Query("SELECT u FROM Utilisateur u JOIN FETCH u.type_utilisateur WHERE u.type_utilisateur.libelle_type_utilisateur = :role")
    List<Utilisateur> findAllByRole(@Param("role") String role);
}
