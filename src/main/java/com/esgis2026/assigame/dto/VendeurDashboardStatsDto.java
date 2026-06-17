package com.esgis2026.assigame.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class VendeurDashboardStatsDto {
    private long totalProduits;
    private long produitsEnAttente;
    private long produitsActifs;
    private double totalVentesEstime;
    private Map<String, Long> produitsParStatut;
    private List<ProduitResumeDto> produitsRecents;
}
