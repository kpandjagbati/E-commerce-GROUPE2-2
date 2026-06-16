package com.esgis2026.assigame.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String role;
    private Long idUtilisateur;
    private String nom;
    private String prenom;
}
