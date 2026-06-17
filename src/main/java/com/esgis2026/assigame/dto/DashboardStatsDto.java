package com.esgis2026.assigame.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class DashboardStatsDto {
    private long totalUtilisateurs;
    private long totalVendeurs;
    private long totalProduits;
    private long produitsEnAttente;
    private long produitsActifs;
    private double chiffreAffairesEstime;
    private Map<String, Long> produitsParStatut;
    private List<ProduitResumeDto> produitsRecents;
    private List<UtilisateurResumeDto> vendeursRecents;
}
