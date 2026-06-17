package com.esgis2026.assigame.controller;

import com.esgis2026.assigame.entity.Produit;
import com.esgis2026.assigame.service.ProduitService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produit")
public class ProduitController {
    final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    public List<Produit> getAllProduit() {
        return produitService.getAllProduits();
    }

    @GetMapping("/mes-produits")
    @PreAuthorize("hasRole('VENDEUR')")
    public List<Produit> getMesProduits() {
        return produitService.getMesProduits();
    }

    @GetMapping("/catalogue")
    public List<Produit> getCatalogueProduits() {
        return produitService.getCatalogueProduits();
    }

    @GetMapping("/catalogue/{id}")
    public Produit getCatalogueProduit(@PathVariable Long id) {
        return produitService.getCatalogueProduitById(id);
    }

    @GetMapping("/catalogue/{id}/image")
    public ResponseEntity<byte[]> getCatalogueProduitImage(@PathVariable Long id) {
        Produit produit = produitService.getCatalogueProduitById(id);
        if (produit.getImage() == null || produit.getImage().length == 0) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (produit.getImage_type() != null) {
            mediaType = MediaType.parseMediaType(produit.getImage_type());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                .body(produit.getImage());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    public Produit getProduitById(@PathVariable Long id) {
        return produitService.getProduitById(id);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    public ResponseEntity<?> createProduit(@RequestPart Produit produit, @RequestPart MultipartFile image) {
        try {
            Produit produit1 = produitService.createProduit(produit, image);
            return new ResponseEntity<>(produit1, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    public void deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    public Produit updateProduit(@RequestBody Produit produit, @PathVariable Long id) {
        return produitService.updateProduit(produit, id);
    }

    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasRole('ADMIN')")
    public Produit updateStatut(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return produitService.updateStatut(id, body.get("statut"));
    }

    @GetMapping("/{id}/image")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    public ResponseEntity<byte[]> getProduitImage(@PathVariable Long id) {
        Produit produit = produitService.getProduitById(id);
        if (produit.getImage() == null || produit.getImage().length == 0) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
        if (produit.getImage_type() != null) {
            mediaType = MediaType.parseMediaType(produit.getImage_type());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mediaType.toString())
                .body(produit.getImage());
    }
}

