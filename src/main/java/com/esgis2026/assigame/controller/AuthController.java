package com.esgis2026.assigame.controller;

import com.esgis2026.assigame.dto.AuthResponse;
import com.esgis2026.assigame.dto.LoginRequest;
import com.esgis2026.assigame.dto.RegisterResponse;
import com.esgis2026.assigame.dto.RegisterVendeurRequest;
import com.esgis2026.assigame.entity.Utilisateur;
import com.esgis2026.assigame.security.CustomUserDetails;
import com.esgis2026.assigame.security.JwtService;
import com.esgis2026.assigame.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UtilisateurService utilisateurService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UtilisateurService utilisateurService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = userDetails.getUtilisateur();
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                utilisateur.getType_utilisateur().getLibelle_type_utilisateur(),
                utilisateur.getId_utilisateur(),
                utilisateur.getNom_utilisateur(),
                utilisateur.getPrenom_utilisateur()
        );
    }

    @PostMapping("/register/vendeur")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse registerVendeur(@RequestBody RegisterVendeurRequest request) {
        Utilisateur vendeur = utilisateurService.registerVendeur(request);
        return new RegisterResponse(
                "Compte vendeur créé avec succès. Connectez-vous pour accéder à votre dashboard.",
                vendeur.getLogin_utilisateur()
        );
    }
}
