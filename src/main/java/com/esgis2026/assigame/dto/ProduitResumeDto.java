package com.esgis2026.assigame.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProduitResumeDto {
    private Long id;
    private String nom;
    private Double prix;
    private String statut;
    private String categorie;
    private String vendeur;
    private LocalDateTime dateAjout;
    private boolean hasImage;
}
